package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Connector")
public class MxGraphConnectorBuilder extends MxGraphNodeBuilder {

	public MxGraphConnectorBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("ellipse", null);
		this.addStyle("html", "1");
		this.setStereotypesVisible(false);
		this.setValue(p.getLabel());
	}
}
