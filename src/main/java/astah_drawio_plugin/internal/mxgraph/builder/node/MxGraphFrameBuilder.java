package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Frame")
public class MxGraphFrameBuilder extends MxGraphNodeBuilder {
	public MxGraphFrameBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("shape", "umlFrame");
		this.addStyle("whiteSpace", "wrap");
		this.addStyle("html", "1");
		this.addStyle("pointerEvents", "0");
		this.addStyle("recursiveResize", "0");
		this.addStyle("container", "1");
		this.addStyle("collapsible", "0");
		this.addStyle("width", "160");
		this.setValue(p.getLabel());
		this.setStereotypesVisible(false);
	}
}
