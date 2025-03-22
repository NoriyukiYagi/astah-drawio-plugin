package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "AcceptEventAction")
public class MxGraphAcceptEventActionBuilder extends MxGraphNodeBuilder {

	public MxGraphAcceptEventActionBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("html", "1");
		this.addStyle("shape", "mxgraph.infographic.ribbonSimple");
		this.addStyle("notch1", "20");
		this.addStyle("notch2", "0");
		this.addStyle("align", "center");
		this.addStyle("verticalAlign", "middle");
		this.addStyle("flipH", "0");
		this.addStyle("spacingRight", "0");
		this.addStyle("spacingLeft", "14");
		this.addStyle("whiteSpace", "wrap");
		this.setValue(p.getLabel());
	}
}
