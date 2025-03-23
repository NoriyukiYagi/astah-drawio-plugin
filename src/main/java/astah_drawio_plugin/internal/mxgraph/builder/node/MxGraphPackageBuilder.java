package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = { "Package", "Subsystem" })
public class MxGraphPackageBuilder extends MxGraphNodeBuilder {

	public MxGraphPackageBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.addStyle("html", "1");
		this.addStyle("shape", "folder");
		this.addStyle("tabWidth", String.valueOf((int) (this.getWidth() * 0.4)));
		this.addStyle("tabHeight", "16");
		this.addStyle("tabPosition", "left");
		this.addStyle("boundedLbl", "1");
		this.addStyle("labelInHeader", "0");
		this.addStyle("container", "1");
		this.addStyle("collapsible", "0");
		this.addStyle("align", "center");
		this.addStyle("verticalAlign", "top");
		this.addStyle("whiteSpace", "wrap");
		this.setValue(p.getLabel());
	}
}
