package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IMultiplicityRange;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Association")
public class MxGraphAssociationBuilder extends MxGraphEdgeBuilder {

	private static String getRoleName(IAttribute attr) {
		var name = attr.getName();
		if (attr.isPrivateVisibility()) {
			return "- " + name;
		} else if (attr.isProtectedVisibility()) {
			return "# " + name;
		} else if (attr.isPackageVisibility()) {
			return "~ " + name;
		} else {
			return "+ " + name;
		}
	}

	private static String toMultiplicityString(int value) {
		switch (value) {
		case IMultiplicityRange.UNDEFINED:
			return "";
		case IMultiplicityRange.UNLIMITED:
			return "*";
		default:
			return String.valueOf(value);
		}
	}

	private static String getMultiplicityString(IAttribute attr) {
		var range = attr.getMultiplicity()[0];
		var lower = toMultiplicityString(range.getLower());
		var upper = toMultiplicityString(range.getUpper());

		if (lower.isEmpty()) {
			lower = range.getLowerString();
		}

		if (upper.isEmpty()) {
			upper = range.getUpperString();
		}

		if (lower.equals(upper)) {
			return lower;
		} else {
			return lower + ".." + upper;
		}
	}

	public MxGraphAssociationBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);

		var props = link.getProperties();
		var isNameDirectionVisible = Boolean.valueOf(props.get("name_direction_visibility").toString());
		var isNameDirectionReverse = Boolean.valueOf(props.get("name_direction_reverse").toString());
		if (isNameDirectionVisible && link.getLabel().length() > 0) {
			var directionString = "";
			var points = link.getPoints();
			var sourcePoint = points[0];
			var targetPoint = points[points.length - 1];

			var x1 = sourcePoint.getX();
			var x2 = targetPoint.getX();
			var y1 = sourcePoint.getY();
			var y2 = targetPoint.getY();
			if (x1 != x2) {
				var atan = Math.atan((y2 - y1) / (x2 - x1));

				if (Math.abs(atan) > Math.toRadians(45)) {
					if (y2 > y1 && !isNameDirectionReverse) {
						directionString = "▲";
					} else {
						directionString = "▼";
					}
				} else if (x2 > x1 && !isNameDirectionReverse) {
					directionString = "▶";
				} else {
					directionString = "◀";
				}
			} else if (y2 > y1 && !isNameDirectionReverse) {
				directionString = "▲";
			} else {
				directionString = "▼";
			}
			this.setValue(link.getLabel() + directionString);
		} else {
			this.setValue(link.getLabel());
		}

		this.setShowLabel(false);

		var model = (IAssociation) link.getModel();

		var startAttr = model.getMemberEnds()[0];
		if (startAttr.isAggregate()) {
			this.addStyle("startArrow", "diamondThin");
			this.addStyle("startFill", "0");
		} else if (startAttr.isComposite()) {
			this.addStyle("startArrow", "diamondThin");
			this.addStyle("startFill", "1");
		} else {
			var startAttrNav = startAttr.getNavigability();
			switch (startAttrNav) {
			case "Navigable":
				this.addStyle("startArrow", "open");
				this.addStyle("startFill", "0");
				break;
			case "Non_Navigable":
				this.addStyle("startArrow", "sysMLx");
				this.addStyle("startFill", "0");
				break;
			default:
				this.addStyle("startArrow", "none");
				break;
			}
		}

		var endAttr = model.getMemberEnds()[1];
		if (endAttr.isAggregate()) {
			this.addStyle("endArrow", "diamondThin");
			this.addStyle("endFill", "0");
		} else if (endAttr.isComposite()) {
			this.addStyle("endArrow", "diamondThin");
			this.addStyle("endFill", "1");
		} else {
			var endAttrNav = endAttr.getNavigability();
			switch (endAttrNav) {
			case "Navigable":
				this.addStyle("endArrow", "open");
				this.addStyle("endFill", "0");
				break;
			case "Non_Navigable":
				this.addStyle("endArrow", "sysMLx");
				this.addStyle("endFill", "0");
				break;
			default:
				this.addStyle("endArrow", "none");
				break;
			}
		}

		this.addStyle("html", "1");
		this.addStyle("labelBackgroundColor", "none");
	}

	public ILinkPresentation getLinkPresentation() {
		return (ILinkPresentation) this.getAstahPresentation();
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);
		this.buildEdgeLabel(graph, parent);
		this.buildStereotypeLabel(graph, parent);
		this.buildEndANameLabel(graph, parent);
		this.buildEndAMultiplicityLabel(graph, parent);
		this.buildEndBNameLabel(graph, parent);
		this.buildEndBMultiplicityLabel(graph, parent);
	}

	private void buildEdgeLabel(mxGraph graph, mxCell parent) {
		if (this.getValue().length() == 0) {
			return;
		}

		var link = this.getLinkPresentation();
		var props = link.getProperties();
		if (!props.containsKey("name.point.x")) {
			return;
		}
		var pointX = Double.valueOf(props.get("name.point.x").toString());
		var pointY = Double.valueOf(props.get("name.point.y").toString());

		var styles = "edgeLabel;html=1;align=left;verticalAlign=top;resizable=0;points=[];";
		var nameText = (mxCell) graph.insertVertex(parent, generateId(), this.getLabel(), 0, 0, 0, 0,
				styles);
		var geo = nameText.getGeometry();
		geo.setX(pointX);
		geo.setY(pointY);
	}

	private void buildStereotypeLabel(mxGraph graph, mxCell parent) {
		var link = this.getLinkPresentation();
		var props = link.getProperties();
		var stereotypes = this.getStereotypes();
		for (var i = 0; i < stereotypes.length; ++i) {
			var pointX = Double.valueOf(props.get("stereotype." + i + ".point.x").toString());
			var pointY = Double.valueOf(props.get("stereotype." + i + ".point.y").toString());

			var styles = "edgeLabel;html=1;align=left;verticalAlign=top;resizable=0;points=[];";
			var label = "&lt;&lt;" + stereotypes[i] + "&gt;&gt;";
			var nameText = (mxCell) graph.insertVertex(parent, generateId(), label, 0, 0, 0, 0,
					styles);
			var geo = nameText.getGeometry();
			geo.setX(pointX);
			geo.setY(pointY);
		}

	}

	private void buildEndANameLabel(mxGraph graph, mxCell parent) {
		var link = this.getLinkPresentation();
		var attr = ((IAssociation) link.getModel()).getMemberEnds()[0];
		var props = link.getProperties();
		if (!props.containsKey("end_a.name.point.x")) {
			return;
		}
		var pointX = Double.valueOf(props.get("end_a.name.point.x").toString());
		var pointY = Double.valueOf(props.get("end_a.name.point.y").toString());

		var styles = "edgeLabel;html=1;align=left;verticalAlign=top;resizable=0;points=[];";
		var nameText = (mxCell) graph.insertVertex(parent, generateId(), getRoleName(attr), 0, 0, 0, 0,
				styles);
		var geo = nameText.getGeometry();
		geo.setX(pointX);
		geo.setY(pointY);
	}

	private void buildEndAMultiplicityLabel(mxGraph graph, mxCell parent) {
		var link = this.getLinkPresentation();
		var attr = ((IAssociation) link.getModel()).getMemberEnds()[0];
		var props = link.getProperties();
		if (!props.containsKey("end_a.multiplicity.point.x")) {
			return;
		}
		var pointX = Double.valueOf(props.get("end_a.multiplicity.point.x").toString());
		var pointY = Double.valueOf(props.get("end_a.multiplicity.point.y").toString());

		var styles = "edgeLabel;html=1;align=left;verticalAlign=top;resizable=0;points=[];";
		var nameText = (mxCell) graph.insertVertex(parent, generateId(), getMultiplicityString(attr), 0, 0, 0, 0,
				styles);
		var geo = nameText.getGeometry();
		geo.setX(pointX);
		geo.setY(pointY);
	}

	private void buildEndBNameLabel(mxGraph graph, mxCell parent) {
		var link = this.getLinkPresentation();
		var attr = ((IAssociation) link.getModel()).getMemberEnds()[1];
		var props = link.getProperties();
		if (!props.containsKey("end_b.name.point.x")) {
			return;
		}
		var pointX = Double.valueOf(props.get("end_b.name.point.x").toString());
		var pointY = Double.valueOf(props.get("end_b.name.point.y").toString());

		var styles = "edgeLabel;html=1;align=left;verticalAlign=top;resizable=0;points=[];";
		var nameText = (mxCell) graph.insertVertex(parent, generateId(), getRoleName(attr), 0, 0, 0, 0,
				styles);
		var geo = nameText.getGeometry();
		geo.setX(pointX);
		geo.setY(pointY);
	}

	private void buildEndBMultiplicityLabel(mxGraph graph, mxCell parent) {
		var link = this.getLinkPresentation();
		var attr = ((IAssociation) link.getModel()).getMemberEnds()[1];
		var props = link.getProperties();
		if (!props.containsKey("end_b.multiplicity.point.x")) {
			return;
		}
		var pointX = Double.valueOf(props.get("end_b.multiplicity.point.x").toString());
		var pointY = Double.valueOf(props.get("end_b.multiplicity.point.y").toString());
		var styles = "edgeLabel;html=1;align=left;verticalAlign=top;resizable=0;points=[];";
		var nameText = (mxCell) graph.insertVertex(parent, generateId(), getMultiplicityString(attr), 0, 0, 0, 0,
				styles);
		var geo = nameText.getGeometry();
		geo.setX(pointX);
		geo.setY(pointY);
	}

}
