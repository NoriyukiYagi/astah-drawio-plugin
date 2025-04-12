package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = { "ControlFlow/ObjectFlow", "Transition" })
public class MxGraphFlowBuilder extends MxGraphEdgeBuilder {

	public MxGraphFlowBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);
		this.addStyle("html", "1");
		this.addStyle("endArrow", "open");
		this.addStyle("startArrow", "none");
		this.addStyle("endFill", "0");
		this.addStyle("startFill", "0");
		this.addStyle("endSize", "8");
		this.addStyle("verticalAlign", "bottom");
	}
}
