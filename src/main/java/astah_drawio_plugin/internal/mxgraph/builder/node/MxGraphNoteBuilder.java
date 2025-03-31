package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Note")
public class MxGraphNoteBuilder extends MxGraphNodeBuilder {

	public MxGraphNoteBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("shape", "note");
		this.addStyle("whiteSpace", "wrap");
		this.addStyle("html", "1");
		this.addStyle("rounded", "0");
		this.addStyle("shadow", "0");
		this.addStyle("comic", "0");
		this.addStyle("align", "left");
		this.addStyle("size", "10");
		this.addStyle("verticalAlign", "top");
		this.setValue(p.getLabel());
	}
}
