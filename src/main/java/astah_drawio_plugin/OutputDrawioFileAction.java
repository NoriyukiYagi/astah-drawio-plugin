package astah_drawio_plugin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import astah_drawio_plugin.internal.ServiceLocator;
import astah_drawio_plugin.internal.converter.AstahDiagramToDrawioConverter;

public class OutputDrawioFileAction implements IPluginActionDelegate {

	@Override
	public Object run(IWindow window) throws UnExpectedException {
		try {
			var api = AstahAPI.getAstahAPI();
			var diagramViewManager = api.getViewManager().getDiagramViewManager();
			var currentDiagram = diagramViewManager.getCurrentDiagram();
			var chooser = new JFileChooser();
			var filter = new FileNameExtensionFilter("Drawio file (*.drawio)", "drawio");
			chooser.setFileFilter(filter);
			if (chooser.showSaveDialog(window.getParent()) == JFileChooser.APPROVE_OPTION) {
				var file = chooser.getSelectedFile();
				var filename = file.toString();
				if (!filename.toLowerCase().endsWith(".drawio")) {
					filename += ".drawio";
				}
				var converter = ServiceLocator.getContainer().get(AstahDiagramToDrawioConverter.class);
				var bytes = converter.convert(currentDiagram);
				Files.write(Paths.get(filename), bytes, StandardOpenOption.CREATE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(window.getParent(), "Unexpected error has occurred.", "Alert",
					JOptionPane.ERROR_MESSAGE);
			throw new UnExpectedException();
		}
		return null;
	}

}
