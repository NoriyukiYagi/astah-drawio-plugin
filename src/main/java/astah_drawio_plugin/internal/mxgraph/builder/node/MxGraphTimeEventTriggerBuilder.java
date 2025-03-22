package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "AcceptTimeEventAction")
public class MxGraphTimeEventTriggerBuilder extends MxGraphNodeBuilder {

	public MxGraphTimeEventTriggerBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("shape", "collate");
		this.addStyle("html", "1");
		this.addStyle("labelPosition", "center");
		this.addStyle("verticalLabelPosition", "bottom");
		this.addStyle("align", "center");
		this.addStyle("verticalAlign", "top");
		this.setValue(p.getLabel());
		this.setStereotypesVisible(false);
	}
}
