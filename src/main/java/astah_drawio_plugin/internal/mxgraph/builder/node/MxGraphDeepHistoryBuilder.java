package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "DeepHistoryPseudostate")
public class MxGraphDeepHistoryBuilder extends MxGraphNodeBuilder {

	public MxGraphDeepHistoryBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("ellipse", null);
		this.addStyle("html", "1");
		this.setValue("H*");
	}
}
