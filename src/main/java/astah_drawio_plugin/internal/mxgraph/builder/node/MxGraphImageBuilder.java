package astah_drawio_plugin.internal.mxgraph.builder.node;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Image")
public class MxGraphImageBuilder extends MxGraphNodeBuilder {

	private static BufferedImage toBufferedImage(Image img) {
		// Create a buffered image with transparency
		var bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		var bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	private static String convertAstahImageToPngImageAsBase64(String astahImageBase64)
			throws IOException, ClassNotFoundException {
		var astahImageData = Base64.getDecoder().decode(astahImageBase64);
		var astahImageDataInputStream = new ObjectInputStream(new ByteArrayInputStream(astahImageData));
		var imageIcon = (ImageIcon) astahImageDataInputStream.readObject();
		var image = toBufferedImage(imageIcon.getImage());
		var imageOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", imageOutputStream);
		return Base64.getEncoder().encodeToString(imageOutputStream.toByteArray());
	}

	public MxGraphImageBuilder(MxGraphNodeBuilder parent, INodePresentation p)
			throws IOException, ClassNotFoundException {
		super(parent, p);
		var astahImageDataBase64 = p.getProperty(PresentationPropertyConstants.Key.IMAGE_DATA);
		var imageData = convertAstahImageToPngImageAsBase64(astahImageDataBase64);
		this.addStyle("shape", "image");
		this.addStyle("aspect", "fixed");
		this.addStyle("imageAspect", "0");
		this.addStyle("image", "data:image/png," + imageData);
		this.setStereotypesVisible(false);
	}
}
