package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Action")
public class MxGraphActionBuilder extends MxGraphNodeBuilder {

	public MxGraphActionBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("html", "1");
		this.addStyle("align", "center");
		this.addStyle("verticalAlign", "middle");
		this.addStyle("rounded", "1");
		this.addStyle("absoluteArcSize", "1");
		this.addStyle("arcSize", "10");
		this.addStyle("dashed", "0");
		this.addStyle("whiteSpace", "wrap");
		this.setValue(p.getLabel());
	}
}
