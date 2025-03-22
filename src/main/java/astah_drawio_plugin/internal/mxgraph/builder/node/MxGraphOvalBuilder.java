package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Oval")
public class MxGraphOvalBuilder extends MxGraphNodeBuilder {

	public MxGraphOvalBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("ellipse", null);
		this.addStyle("html", "1");

		var isFilledString = p.getProperty("isfilled.color");
		var isFilled = (isFilledString != null) && Boolean.parseBoolean(isFilledString);
		if (!isFilled) {
			this.removeStyle("fillColor");
		}

		this.setValue(p.getLabel());
	}
}
