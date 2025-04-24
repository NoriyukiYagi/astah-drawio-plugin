package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;
import astah_drawio_plugin.internal.utils.TextGeometryCalculator;

@GraphElementBuilder(astahTypes = "Frame")
public class MxGraphFrameBuilder extends MxGraphNodeBuilder {
	private static final int LABEL_WIDTH_MARGIN = 20;

	public MxGraphFrameBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.setStereotypesVisible(false);
		this.setValue(p.getLabel());
		var labelWidth = TextGeometryCalculator.getTextWidth(this.getLabel());
		this.addStyle("shape", "umlFrame");
		this.addStyle("whiteSpace", "wrap");
		this.addStyle("html", "1");
		this.addStyle("pointerEvents", "0");
		this.addStyle("recursiveResize", "0");
		this.addStyle("container", "1");
		this.addStyle("collapsible", "0");
		this.addStyle("width", String.valueOf(labelWidth + LABEL_WIDTH_MARGIN));
	}
}
