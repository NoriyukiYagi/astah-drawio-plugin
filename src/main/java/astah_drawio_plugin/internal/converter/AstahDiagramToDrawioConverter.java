package astah_drawio_plugin.internal.converter;

import java.lang.reflect.InvocationTargetException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IDiagram;

public class AstahDiagramToDrawioConverter {

	private AstahDiagramToMxGraphConverter astahDiagramToMxGraphConverter;
	private MxGraphToDrawioConverter mxGraphToDrawioConverter;

	public AstahDiagramToDrawioConverter(AstahDiagramToMxGraphConverter astahDiagramToMxGraphConverter,
			MxGraphToDrawioConverter mxGraphToDrawioConverter) {
		this.astahDiagramToMxGraphConverter = astahDiagramToMxGraphConverter;
		this.mxGraphToDrawioConverter = mxGraphToDrawioConverter;
	}

	public byte[] convert(IDiagram diagram)
			throws InvalidUsingException, TransformerFactoryConfigurationError, TransformerException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var graph = this.astahDiagramToMxGraphConverter.convert(diagram);
		return this.mxGraphToDrawioConverter.convert(graph);
	}
}
