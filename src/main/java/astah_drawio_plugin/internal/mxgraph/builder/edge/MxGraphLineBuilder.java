package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = { "Line", "FreeHand" })
public class MxGraphLineBuilder extends MxGraphEdgeBuilder {

	public MxGraphLineBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);
		this.addStyle("html", "1");
		this.addStyle("rounded", "1");

		switch (link.getProperty(PresentationPropertyConstants.Key.LINE_ARROW_TYPE)) {
		case PresentationPropertyConstants.Value.LINE_ARROW_TYPE_BOTH_ARROW:
			this.addStyle("startArrow", "open");
			this.addStyle("endArrow", "open");
			break;
		case PresentationPropertyConstants.Value.LINE_ARROW_TYPE_START_ARROW:
			this.addStyle("startArrow", "open");
			this.addStyle("endArrow", "none");
			break;
		case PresentationPropertyConstants.Value.LINE_ARROW_TYPE_END_ARROW:
			this.addStyle("startArrow", "none");
			this.addStyle("endArrow", "open");
			break;
		default:
			this.addStyle("startArrow", "none");
			this.addStyle("endArrow", "none");
			break;
		}
	}
}
