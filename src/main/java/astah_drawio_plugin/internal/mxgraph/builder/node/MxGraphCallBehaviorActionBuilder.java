package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "CallBehaviorAction")
public class MxGraphCallBehaviorActionBuilder extends MxGraphNodeBuilder {

	public MxGraphCallBehaviorActionBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("shape", "mxgraph.uml25.behaviorAction");
		this.addStyle("html", "1");
		this.addStyle("rounded", "1");
		this.addStyle("absoluteArcSize", "1");
		this.addStyle("arcSize", "10");
		this.addStyle("align", "left");
		this.addStyle("spacingLeft", "5");
		this.addStyle("whiteSpace", "wrap");
		this.setValue(p.getLabel());
	}
}
