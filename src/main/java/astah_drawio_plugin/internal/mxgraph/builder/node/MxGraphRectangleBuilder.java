package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Rectangle")
public class MxGraphRectangleBuilder extends MxGraphNodeBuilder {

	public MxGraphRectangleBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("html", "1");

		var isFilledString = p.getProperty("isfilled.color");
		var isFilled = (isFilledString != null) && Boolean.parseBoolean(isFilledString);
		if (!isFilled) {
			this.removeStyle("fillColor");
		}

		var rectType = p.getProperty(PresentationPropertyConstants.Key.RECT_TYPE);
		if ("round".equals(rectType)) {
			this.addStyle("rounded", "1");
		} else {
			this.addStyle("rounded", "0");
		}

		this.setValue(p.getLabel());
	}
}
