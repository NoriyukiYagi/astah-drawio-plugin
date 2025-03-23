package astah_drawio_plugin.internal;

import astah_drawio_plugin.internal.converter.AstahDiagramToDrawioConverter;
import astah_drawio_plugin.internal.converter.AstahDiagramToMxGraphConverter;
import astah_drawio_plugin.internal.converter.MxGraphToDrawioConverter;
import astah_drawio_plugin.internal.di.DIContainer;
import astah_drawio_plugin.internal.di.DIContainerModule;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphAssociationBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphContainmentBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphDependencyBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphFlowBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphGeneralizationBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphHighlighterBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphLineBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphNoteAnchorBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphRealizationBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.edge.MxGraphUsageBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.factory.MxGraphEdgeBuilderFactoryMap;
import astah_drawio_plugin.internal.mxgraph.builder.factory.MxGraphNodeBuilderFactoryMap;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphAcceptEventActionBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphActionBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphActivityFinalBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphCallBehaviorActionBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphClassBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphConnectorBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphDecisionMergeNodeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphFlowFinalNodeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphForkJoinBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphFrameBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphImageBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphInitialNodeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphNoteBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphObjectNodeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphOvalBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphPackageBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphPartitionBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphPinBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphProcessObjectNodeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphRectangleBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphSendSignalActionBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphTextBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.node.MxGraphTimeEventTriggerBuilder;

public class AppModule implements DIContainerModule {

	private DIContainer container;

	public AppModule(DIContainer container) {
		this.container = container;
	}

	@Override
	public void configure() throws NoSuchMethodException, SecurityException {
		this.configureNodeBuilderFactory();
		this.configureEdgeBuilderFactory();
		this.configureConverter();
	}

	private void configureNodeBuilderFactory() throws NoSuchMethodException, SecurityException {
		var map = new MxGraphNodeBuilderFactoryMap();
		map.putDefault(MxGraphAcceptEventActionBuilder.class);
		map.putDefault(MxGraphActionBuilder.class);
		map.putDefault(MxGraphActivityFinalBuilder.class);
		map.putDefault(MxGraphCallBehaviorActionBuilder.class);
		map.putDefault(MxGraphClassBuilder.class);
		map.putDefault(MxGraphConnectorBuilder.class);
		map.putDefault(MxGraphDecisionMergeNodeBuilder.class);
		map.putDefault(MxGraphFlowFinalNodeBuilder.class);
		map.putDefault(MxGraphForkJoinBuilder.class);
		map.putDefault(MxGraphFrameBuilder.class);
		map.putDefault(MxGraphImageBuilder.class);
		map.putDefault(MxGraphInitialNodeBuilder.class);
		map.putDefault(MxGraphNoteBuilder.class);
		map.putDefault(MxGraphObjectNodeBuilder.class);
		map.putDefault(MxGraphOvalBuilder.class);
		map.putDefault(MxGraphPackageBuilder.class);
		map.putDefault(MxGraphPartitionBuilder.class);
		map.putDefault(MxGraphPinBuilder.class);
		map.putDefault(MxGraphProcessObjectNodeBuilder.class);
		map.putDefault(MxGraphRectangleBuilder.class);
		map.putDefault(MxGraphSendSignalActionBuilder.class);
		map.putDefault(MxGraphTextBuilder.class);
		map.putDefault(MxGraphTimeEventTriggerBuilder.class);
		this.container.registerInstance(MxGraphNodeBuilderFactoryMap.class, map);
	}

	private void configureEdgeBuilderFactory() throws NoSuchMethodException, SecurityException {
		var map = new MxGraphEdgeBuilderFactoryMap();
		map.putDefault(MxGraphAssociationBuilder.class);
		map.putDefault(MxGraphContainmentBuilder.class);
		map.putDefault(MxGraphDependencyBuilder.class);
		map.putDefault(MxGraphFlowBuilder.class);
		map.putDefault(MxGraphGeneralizationBuilder.class);
		map.putDefault(MxGraphHighlighterBuilder.class);
		map.putDefault(MxGraphLineBuilder.class);
		map.putDefault(MxGraphNoteAnchorBuilder.class);
		map.putDefault(MxGraphRealizationBuilder.class);
		map.putDefault(MxGraphUsageBuilder.class);
		this.container.registerInstance(MxGraphEdgeBuilderFactoryMap.class, map);
	}

	private void configureConverter() throws SecurityException {
		this.container.register(AstahDiagramToDrawioConverter.class);
		this.container.register(AstahDiagramToMxGraphConverter.class);
		this.container.register(MxGraphToDrawioConverter.class);
	}

}
