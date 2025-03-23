package astah_drawio_plugin.internal.mxgraph.builder.edge;

import java.util.Arrays;
import java.util.HashSet;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Realization")
public class MxGraphRealizationBuilder extends MxGraphEdgeBuilder {

	public MxGraphRealizationBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);
		this.setStereotypesVisible(false);

		if (this.isConnectedToInterfaceIcon()) {
			this.addStyle("html", "1");
			this.addStyle("startArrow", "none");
			this.addStyle("endArrow", "none");
		} else {
			this.addStyle("html", "1");
			this.addStyle("startArrow", "block");
			this.addStyle("startFill", "0");
			this.addStyle("startSize", "8");
			this.addStyle("endArrow", "none");
			this.addStyle("labelBackgroundColor", "none");
			this.addStyle("dashed", "1");
		}
	}

	private boolean isConnectedToInterfaceIcon() {
		var link = (ILinkPresentation) this.getAstahPresentation();
		var source = link.getSource();
		var notationType = source.getProperty(PresentationPropertyConstants.Key.NOTATION_TYPE);
		var stereotypes = new HashSet<>(Arrays.asList(source.getModel().getStereotypes()));
		return "icon".equals(notationType) && stereotypes.contains("interface");
	}
}
