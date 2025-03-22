package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "SendSignalAction")
public class MxGraphSendSignalActionBuilder extends MxGraphNodeBuilder {

	public MxGraphSendSignalActionBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("html", "1");
		this.addStyle("shape", "mxgraph.infographic.ribbonSimple");
		this.addStyle("notch1", "0");
		this.addStyle("notch2", "20");
		this.addStyle("align", "center");
		this.addStyle("verticalAlign", "middle");
		this.addStyle("whiteSpace", "wrap");
		this.setValue(p.getLabel());
	}
}
