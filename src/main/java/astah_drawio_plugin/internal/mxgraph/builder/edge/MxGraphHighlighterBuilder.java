package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Highlighter")
public class MxGraphHighlighterBuilder extends MxGraphEdgeBuilder {

	public MxGraphHighlighterBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);
		this.addStyle("html", "1");
		this.addStyle("startArrow", "none");
		this.addStyle("endArrow", "none");
		this.addStyle("rounded", "1");

		var opacityStr = link.getProperty(PresentationPropertyConstants.Key.OPACITY);
		var opacity = Double.parseDouble(opacityStr);
		this.addStyle("opacity", String.valueOf((int) (opacity * 100)));
	}
}
