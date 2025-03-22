package astah_drawio_plugin.internal.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.factory.MxGraphEdgeBuilderFactory;
import astah_drawio_plugin.internal.mxgraph.builder.factory.MxGraphEdgeBuilderFactoryMap;
import astah_drawio_plugin.internal.mxgraph.builder.factory.MxGraphNodeBuilderFactory;
import astah_drawio_plugin.internal.mxgraph.builder.factory.MxGraphNodeBuilderFactoryMap;

public class AstahDiagramToMxGraphConverter {

	private static final Logger logger = Logger.getLogger(AstahDiagramToMxGraphConverter.class.getName());

	private MxGraphNodeBuilderFactoryMap nodeBuilderFactoryMap;
	private MxGraphEdgeBuilderFactoryMap edgeBuilderFactoryMap;

	private HashMap<String, MxGraphNodeBuilderFactory> getAstahTypeToNodeBuilderFactoryMap() {
		var result = new HashMap<String, MxGraphNodeBuilderFactory>();
		for (var entry : this.nodeBuilderFactoryMap) {
			var annotation = entry.getKey().getAnnotation(GraphElementBuilder.class);
			if (annotation != null) {
				for (var astahType : annotation.astahTypes()) {
					result.put(astahType, entry.getValue());
				}
			} else {
				var message = "Class " + entry.getKey().getName() + " does not have GraphElementBuilder annotation";
				logger.severe(message);
			}
		}
		return result;
	}

	private HashMap<INodePresentation, MxGraphNodeBuilder> createMxGraphNodeBuilders(IDiagram diagram)
			throws InvalidUsingException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		var result = new HashMap<INodePresentation, MxGraphNodeBuilder>();

		var astahTypeToBuilderFactoryMap = this.getAstahTypeToNodeBuilderFactoryMap();

		var astahNodes = new LinkedList<>(Arrays.stream(diagram.getPresentations())
				.filter(INodePresentation.class::isInstance).map(INodePresentation.class::cast).toList());

		while (!astahNodes.isEmpty()) {
			var astahNode = astahNodes.remove();
			var astahNodeParent = astahNode.getParent();
			MxGraphNodeBuilder parentBuilder = null;
			if (astahNodeParent != null) {
				if (!result.containsKey(astahNodeParent)) {
					astahNodes.add(astahNode);
					continue;
				} else {
					parentBuilder = result.get(astahNodeParent);
				}
			}

			var astahNodeType = astahNode.getType();
			if (astahTypeToBuilderFactoryMap.containsKey(astahNodeType)) {
				var factory = astahTypeToBuilderFactoryMap.get(astahNodeType);
				result.put(astahNode, factory.create(parentBuilder, astahNode));
			} else {
				logger.severe("Unsupported Node Type: " + astahNode.getType());
				result.put(astahNode, new MxGraphNodeBuilder(parentBuilder, astahNode));
			}
		}
		return result;
	}

	private HashMap<String, MxGraphEdgeBuilderFactory> getAstahTypeToEdgeBuilderFactoryMap() {
		var result = new HashMap<String, MxGraphEdgeBuilderFactory>();
		for (var entry : this.edgeBuilderFactoryMap) {
			var annotation = entry.getKey().getAnnotation(GraphElementBuilder.class);
			if (annotation != null) {
				for (var astahType : annotation.astahTypes()) {
					result.put(astahType, entry.getValue());
				}
			} else {
				var message = "Class " + entry.getKey().getName() + " does not have GraphElementBuilder annotation";
				logger.severe(message);
			}
		}
		return result;
	}

	private List<MxGraphEdgeBuilder> createMxGraphEdgeBuilders(IDiagram diagram,
			HashMap<INodePresentation, MxGraphNodeBuilder> nodesDict) throws InvalidUsingException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var result = new LinkedList<MxGraphEdgeBuilder>();

		var astahTypeToBuilderFactoryMap = this.getAstahTypeToEdgeBuilderFactoryMap();

		var astahLinks = new LinkedList<>(Arrays.stream(diagram.getPresentations())
				.filter(ILinkPresentation.class::isInstance).map(ILinkPresentation.class::cast).toList());

		while (!astahLinks.isEmpty()) {
			var astahLink = astahLinks.remove();
			var source = astahLink.getSource();
			var target = astahLink.getTarget();
			var astahLinkType = astahLink.getType();
			if (astahTypeToBuilderFactoryMap.containsKey(astahLinkType)) {
				var factory = astahTypeToBuilderFactoryMap.get(astahLinkType);
				result.add(factory.create(astahLink, nodesDict.get(source), nodesDict.get(target)));
			} else {
				logger.severe("Unsupported Link Type: " + astahLink.getType());
				result.add(new MxGraphEdgeBuilder(astahLink, nodesDict.get(source), nodesDict.get(target)));
			}
		}
		return result;
	}

	public AstahDiagramToMxGraphConverter(MxGraphNodeBuilderFactoryMap nodeBuilderFactoryMap,
			MxGraphEdgeBuilderFactoryMap edgeBuilderFactoryMap) {
		this.nodeBuilderFactoryMap = nodeBuilderFactoryMap;
		this.edgeBuilderFactoryMap = edgeBuilderFactoryMap;
	}

	public mxGraph convert(IDiagram diagram) throws InvalidUsingException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var nodeBuilders = createMxGraphNodeBuilders(diagram);
		var edgeBuilders = createMxGraphEdgeBuilders(diagram, nodeBuilders);

		var graph = new mxGraph();
		var graphModel = graph.getModel();

		graphModel.beginUpdate();
		try {
			for (var builder : nodeBuilders.values()) {
				builder.buildIfNeeded(graph);
			}
			for (var builder : edgeBuilders) {
				builder.buildIfNeeded(graph);
			}
		} finally {
			graphModel.endUpdate();
		}

		return graph;
	}
}
