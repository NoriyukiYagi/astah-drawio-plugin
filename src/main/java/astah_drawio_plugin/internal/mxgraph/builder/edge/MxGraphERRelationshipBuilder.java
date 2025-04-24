package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.model.IERRelationship;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = { "Indentyfying-Relationship", "Non-Indentyfying-Relationship",
		"Many-to-many-Relationship" })
public class MxGraphERRelationshipBuilder extends MxGraphEdgeBuilder {

	public MxGraphERRelationshipBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);

		var model = (IERRelationship) link.getModel();
		this.setValue(null);
		this.addStyle("html", "1");
		this.addStyle("rounded", "0");

		if (model.isMultiToMulti()) {
			this.addStyle("startArrow", "oval");
			this.addStyle("startFill", "1");
			this.addStyle("endArrow", "oval");
			this.addStyle("endFill", "1");
			this.addStyle("dashed", "0");
			this.addStyle("targetPerimeterSpacing", "4");
			this.addStyle("sourcePerimeterSpacing", "4");
		} else {
			if (model.isParentRequired()) {
				this.addStyle("startArrow", "none");
				this.addStyle("startFill", "0");
				this.addStyle("endArrow", "oval");
				this.addStyle("endFill", "1");
				this.addStyle("targetPerimeterSpacing", "4");
			} else {
				this.addStyle("startArrow", "diamondThin");
				this.addStyle("startFill", "0");
				this.addStyle("startSize", "12");
				this.addStyle("endArrow", "oval");
				this.addStyle("endFill", "1");
				this.addStyle("targetPerimeterSpacing", "4");
			}
			this.addStyle("dashed", model.isIdentifying() ? "0" : "1");
		}
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);

		var model = (IERRelationship) this.getAstahPresentation().getModel();
		switch (model.getCardinality()) {
		case "0orMore":
			break;
		case "1orMore":
			this.buildCardinality(graph, parent, "P");
			break;
		case "0or1":
			this.buildCardinality(graph, parent, "Z");
			break;
		default:
			this.buildCardinality(graph, parent, model.getCardinality());
			break;
		}

	}

	private void buildCardinality(mxGraph graph, mxCell parent, String label) {
		var styles = "edgeLabel;html=1;align=center;verticalAlign=middle;resizable=0;";
		var nameText = (mxCell) graph.insertVertex(parent, generateId(), label, 0, 0, 0, 0,
				styles);
		var geo = nameText.getGeometry();
		geo.setX(0.7);
		geo.setY(-10);
		geo.setRelative(true);
	}
}
