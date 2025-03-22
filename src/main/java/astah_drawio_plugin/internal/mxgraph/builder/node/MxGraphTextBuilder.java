package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Text")
public class MxGraphTextBuilder extends MxGraphNodeBuilder {

	public MxGraphTextBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("text", null);
		this.addStyle("html", "1");
		this.addStyle("align", "left");
		this.addStyle("verticalAlign", "top");
		this.addStyle("whiteSpace", "wrap");

		var shape = p.getProperty(PresentationPropertyConstants.Key.SHAPE);
		if (shape == null) {
			this.removeStyle("fillColor");
			this.removeStyle("strokeColor");
			this.removeStyle("strokeWidth");
		} else {
			var isFilledString = p.getProperty("isfilled.color");
			var isFilled = (isFilledString != null) && Boolean.parseBoolean(isFilledString);

			if (!isFilled) {
				this.removeStyle("fillColor");
			}

			if (shape.equals("border_none")) {
				this.removeStyle("strokeColor");
				this.removeStyle("strokeWidth");
			}
		}

		this.addStyle("rounded", "0");
		this.setValue(p.getLabel());
	}
}
