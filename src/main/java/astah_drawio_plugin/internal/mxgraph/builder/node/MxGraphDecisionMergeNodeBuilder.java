package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Decision Node & Merge Node")
public class MxGraphDecisionMergeNodeBuilder extends MxGraphNodeBuilder {

	public MxGraphDecisionMergeNodeBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("rhombus", null);
		this.setStereotypesVisible(false);
	}
}
