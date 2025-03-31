package astah_drawio_plugin;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.NonCompatibleException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IDiagram;

import astah_drawio_plugin.internal.AppModule;
import astah_drawio_plugin.internal.converter.AstahDiagramToMxGraphConverter;
import astah_drawio_plugin.internal.converter.MxGraphToDrawioConverter;
import astah_drawio_plugin.internal.di.DIContainer;

public class ConvertAstahToDrawioTest {

	public static final String DRAWIO_EXE = "C:/Program Files/draw.io/draw.io.exe";

	public double difference(Color c1, Color c2) {
		double rd = (c1.getRed() - c2.getRed()) / 255.0;
		double gd = (c1.getGreen() - c2.getGreen()) / 255.0;
		double bd = (c1.getBlue() - c2.getBlue()) / 255.0;

		return Math.sqrt(rd * rd + gd * gd + bd * bd) / 3;
	}

	public static List<IDiagram> getDiagrams(InputStream inputStream)
			throws ClassNotFoundException, LicenseNotFoundException, ProjectNotFoundException, NonCompatibleException,
			IOException, ProjectLockedException {
		var projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		projectAccessor.open(inputStream);
		var project = projectAccessor.getProject();
		var diagrams = project.getDiagrams();
		return Arrays.asList(diagrams);
	}

	private void testDiagram(String resourceBasePath, String baseName, String testResultPath, boolean expectedPngExists)
			throws Exception {
		var testResultAstah = Paths.get(testResultPath, baseName + ".asta");
		var testResultDrawio = Paths.get(testResultPath, baseName + ".drawio");
		var testResultPng = Paths.get(testResultPath, baseName + ".png");
		var testResultExpectedPng = Paths.get(testResultPath, baseName + ".expected.png");
		var resourceAstah = resourceBasePath + baseName + ".asta";
		var resourcePng = resourceBasePath + baseName + ".png";

		// Arrange
		var container = new DIContainer(AppModule.class);
		Files.createDirectories(Paths.get(testResultPath));
		var converter = container.get(AstahDiagramToMxGraphConverter.class);
		var inputStream = getClass().getClassLoader().getResourceAsStream(resourceAstah);
		var diagrams = getDiagrams(inputStream);
		// -- Oputput astah
		Files.copy(getClass().getClassLoader().getResourceAsStream(resourceAstah), testResultAstah,
				StandardCopyOption.REPLACE_EXISTING);
		if (expectedPngExists) {
			// -- Oputput expected PNG
			Files.copy(getClass().getClassLoader().getResourceAsStream(resourcePng), testResultExpectedPng,
					StandardCopyOption.REPLACE_EXISTING);
		}

		// Assume
		assertEquals(1, diagrams.size());

		// Act
		var graph = converter.convert(diagrams.get(0));

		// -- Output drawio
		var drawioConv = container.get(MxGraphToDrawioConverter.class);
		var fileData = drawioConv.convert(graph);
		Files.write(testResultDrawio, fileData);

		// -- Export PNG
		var process = Runtime.getRuntime().exec(
				new String[] { DRAWIO_EXE, "-xf", "png", "-o", testResultPng.toString(), testResultDrawio.toString() });
		process.waitFor();

		// Assert
		if (expectedPngExists) {
			var expectedImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(resourcePng));
			try (var exppectedStream = new ByteArrayOutputStream()) {
				ImageIO.write(expectedImage, "bmp", exppectedStream);
			}

			var actualImage = ImageIO.read(testResultPng.toFile());
			try (var actualStream = new ByteArrayOutputStream()) {
				ImageIO.write(actualImage, "bmp", actualStream);
			}

			// -- Compare Image
			assertEquals(expectedImage.getWidth(), actualImage.getWidth());
			assertEquals(expectedImage.getHeight(), actualImage.getHeight());
			int max = 0;
			for (var x = 0; x < expectedImage.getWidth(); ++x) {
				for (var y = 0; y < expectedImage.getHeight(); ++y) {
					var expectedColor = new Color(expectedImage.getRGB(x, y));
					var actualColor = new Color(actualImage.getRGB(x, y));
					var diff = (int) (difference(expectedColor, actualColor) * 100);
					assertTrue(diff <= 25,
							"Color difference greater than 25%% (x,y)=(%d,%d) diff=%d%%".formatted(x, y, diff));
					if (diff > max) {
						max = diff;
					}
				}
			}
			if (max > 0) {
				System.out.println("[%s] Max color difference: %d%%".formatted(baseName, max));
			}
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "SimpleDiagram", "CurvedLine", "Pin", "ForkJoin", "DecisionMerge", "SwimLane", "Partition",
			"Partition2", "Signal", "Note", "Image", "FlowFinalNode", "Common", "CallBehaviorAction", "ObjectFlow",
			"Connector", "TimeEvent", "Process", "Dependency" })
	void testActivityDiagram(String baseName) throws Exception {
		testDiagram("activity/", baseName, "./test-result/activity", true);
	}

	@ParameterizedTest
	@ValueSource(strings = { "SimpleDiagram", "Association", "Enumeration", "Package", "Icon", "Visibility",
			"Quantifier", "TemplateParam" })
	void testClassDiagram(String baseName) throws Exception {
		testDiagram("class/", baseName, "./test-result/class", true);
	}
}
