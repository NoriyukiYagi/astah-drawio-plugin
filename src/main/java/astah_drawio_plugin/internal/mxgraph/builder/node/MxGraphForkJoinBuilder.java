package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = { "ForkNode", "JoinNode" })
public class MxGraphForkJoinBuilder extends MxGraphNodeBuilder {

	public MxGraphForkJoinBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		var color = p.getProperty(PresentationPropertyConstants.Key.LINE_COLOR);
		this.addStyle("html", "1");
		this.addStyle("points", "[]");
		this.addStyle("perimeter", "orthogonalPerimeter");
		this.addStyle("html", "1");
		this.addStyle("fillColor", color);
		this.setStereotypesVisible(false);
	}
}
