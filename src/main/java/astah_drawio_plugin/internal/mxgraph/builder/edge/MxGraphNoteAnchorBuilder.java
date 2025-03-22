package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "NoteAnchor")
public class MxGraphNoteAnchorBuilder extends MxGraphEdgeBuilder {

	public MxGraphNoteAnchorBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);
		this.addStyle("html", "1");
		this.addStyle("dashed", "1");
		this.addStyle("startArrow", "none");
		this.addStyle("startFill", "0");
		this.addStyle("startSize", "5");
		this.addStyle("endArrow", "oval");
		this.addStyle("endFill", "0");
		this.addStyle("endSize", "5");
		this.addStyle("jettySize", "auto");
		this.addStyle("orthogonalLoop", "1");
	}
}
