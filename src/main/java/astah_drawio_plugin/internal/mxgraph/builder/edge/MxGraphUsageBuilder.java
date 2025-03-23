package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Usage")
public class MxGraphUsageBuilder extends MxGraphEdgeBuilder {

	public MxGraphUsageBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);
		this.setStereotypesVisible(false);
		this.addStyle("html", "1");
		this.addStyle("startArrow", "halfCircle");
		this.addStyle("startFill", "0");
		this.addStyle("startSize", "8");
		this.addStyle("endArrow", "none");
		this.addStyle("endFill", "0");
	}
}
