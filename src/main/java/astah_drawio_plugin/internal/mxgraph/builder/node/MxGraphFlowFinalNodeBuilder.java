package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "FlowFinalNode")
public class MxGraphFlowFinalNodeBuilder extends MxGraphNodeBuilder {

	public MxGraphFlowFinalNodeBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("shape", "sumEllipse");
		this.addStyle("perimeter", "ellipsePerimeter");
		this.addStyle("html", "1");
		this.addStyle("backgroundOutline", "1");
		this.setStereotypesVisible(false);
	}
}
