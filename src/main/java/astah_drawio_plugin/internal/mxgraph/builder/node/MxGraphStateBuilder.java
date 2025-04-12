package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;
import astah_drawio_plugin.internal.utils.TextGeometryCalculator;

@GraphElementBuilder(astahTypes = "State")
public class MxGraphStateBuilder extends MxGraphNodeBuilder {

	private static int LABEL_MARGIN = 2;
	private static int MAX_ARCSIZE = 20;

	public MxGraphStateBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.setValue(p.getLabel());
		var labelHeight = TextGeometryCalculator.getTextHeight(this.getLabel());
		var arcSize = Math.min(Math.min(getWidth(), getHeight()) / 5, MAX_ARCSIZE);
		this.addStyle("swimlane", null);
		this.addStyle("align", "center");
		this.addStyle("verticalAlign", "top");
		this.addStyle("horizontal", "1");
		this.addStyle("startSize", String.valueOf(labelHeight + LABEL_MARGIN * 2));
		this.addStyle("collapsible", "0");
		this.addStyle("html", "1");
		this.addStyle("rounded", "1");
		this.addStyle("absoluteArcSize", "1");
		this.addStyle("arcSize", String.valueOf(arcSize));
		this.addStyle("whiteSpace", "wrap");
		this.addStyle("spacing", "0");
		this.addStyle("swimlaneFillColor", this.getStyle("fillColor"));
	}
}
