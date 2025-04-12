package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = { "ActivityFinal", "FinalState" })
public class MxGraphFinalNodeBuilder extends MxGraphNodeBuilder {

	public MxGraphFinalNodeBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("html", "1");
		this.addStyle("shape", "endState");
		this.addStyle("fillColor", "strokeColor");
		this.setStereotypesVisible(false);
	}
}
