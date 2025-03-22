package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = { "OutputPin", "InputPin" })
public class MxGraphPinBuilder extends MxGraphNodeBuilder {

	public MxGraphPinBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("html", "1");
		this.addStyle("verticalAlign", "middle");
		this.addStyle("spacingLeft", "2");

		var pTop = parent.getY();
		var pBottom = pTop + parent.getHeight();
		if (pTop <= this.getY() && this.getY() + this.getHeight() <= pBottom) {
			if (this.getCenterX() < parent.getCenterX()) {
				// left side position
				this.addStyle("labelPosition", "middle");
				this.addStyle("verticalLabelPosition", "bottom");
				this.addStyle("align", "right");
				this.addStyle("spacingTop", "5");
			} else {
				// right side position
				this.addStyle("labelPosition", "middle");
				this.addStyle("verticalLabelPosition", "bottom");
				this.addStyle("align", "left");
				this.addStyle("spacingTop", "5");
			}
		} else {
			if (this.getY() < pTop) {
				// top position
				this.addStyle("labelPosition", "right");
				this.addStyle("verticalLabelPosition", "middle");
				this.addStyle("align", "left");
				this.addStyle("spacingBottom", "5");
			} else {
				// bottom position
				this.addStyle("labelPosition", "right");
				this.addStyle("verticalLabelPosition", "middle");
				this.addStyle("align", "left");
				this.addStyle("spacingTop", "5");
			}
		}
		this.setValue(p.getLabel());
	}
}
