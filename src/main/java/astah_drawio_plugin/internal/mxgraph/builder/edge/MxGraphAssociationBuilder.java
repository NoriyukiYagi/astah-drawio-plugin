package astah_drawio_plugin.internal.mxgraph.builder.edge;

import java.util.ArrayList;

import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IMultiplicityRange;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;
import astah_drawio_plugin.internal.utils.TextGeometryCalculator;

@GraphElementBuilder(astahTypes = "Association")
public class MxGraphAssociationBuilder extends MxGraphEdgeBuilder {

	private static int QUANTIFIER_PADDING = 8;

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

	private static void createGroup(mxGraph graph, mxCell cell1, mxCell cell2) {
		var groupParent = cell1.getParent();
		var geo1 = cell1.getGeometry();
		var geo2 = cell2.getGeometry();
		var groupX = Math.min(geo1.getX(), geo2.getX());
		var groupY = Math.min(geo1.getY(), geo2.getY());
		var groupW = Math.max(geo1.getX() + geo1.getWidth(), geo2.getX() + geo2.getWidth()) - groupX;
		var groupH = Math.max(geo1.getY() + geo1.getHeight(), geo2.getY() + geo2.getHeight()) - groupY;
		var group = (mxCell) graph.insertVertex(groupParent, generateId(), "", groupX, groupY, groupW,
				groupH, "group");
		cell1.setParent(group);
		geo1.setX(geo1.getX() - groupX);
		geo1.setY(geo1.getY() - groupY);
		cell2.setParent(group);
		geo2.setX(geo2.getX() - groupX);
		geo2.setY(geo2.getY() - groupY);
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
						directionString = "▼";
					} else {
						directionString = "▲";
					}
				} else if (x2 > x1 && !isNameDirectionReverse) {
					directionString = "▶";
				} else {
					directionString = "◀";
				}
			} else if (y2 > y1 && !isNameDirectionReverse) {
				directionString = "▼";
			} else {
				directionString = "▲";
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
		this.buildEndAQuantifier(graph, parent);
		this.buildEndBNameLabel(graph, parent);
		this.buildEndBMultiplicityLabel(graph, parent);
		this.buildEndBQuantifier(graph, parent);
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
		for (var i = 0; i < stereotypes.size(); ++i) {
			var pointX = Double.valueOf(props.get("stereotype." + i + ".point.x").toString());
			var pointY = Double.valueOf(props.get("stereotype." + i + ".point.y").toString());

			var styles = "edgeLabel;html=1;align=left;verticalAlign=top;resizable=0;points=[];";
			var label = "&lt;&lt;" + stereotypes.get(i) + "&gt;&gt;";
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

	private mxCell buildQuantifier(mxGraph graph, IAttribute attr) {
		if (attr.getQualifiers().length == 0) {
			return null;
		}

		var lines = new ArrayList<String>();
		int maxLineWidth = 0;
		for (var qualifier : attr.getQualifiers()) {
			var line = qualifier.getName() + " : " + qualifier.getTypeExpression();
			lines.add(line);
			var lineWidth = TextGeometryCalculator.getTextWidth(line);
			if (lineWidth > maxLineWidth) {
				maxLineWidth = lineWidth;
			}
		}

		var height = lines.size() * TextGeometryCalculator.getTextHeight();
		var width = maxLineWidth + QUANTIFIER_PADDING * 2;
		var label = String.join("\n", lines);

		var styles = "fontStyle=0;html=1;whiteSpace=wrap;";
		var quantifier = (mxCell) graph.insertVertex(graph.getDefaultParent(), generateId(), label, 0, 0, 0,
				0,
				styles);
		var geo = quantifier.getGeometry();
		geo.setWidth(width);
		geo.setHeight(height);
		return quantifier;
	}

	private void buildEndAQuantifier(mxGraph graph, mxCell parent) {
		var link = this.getLinkPresentation();
		var attr = ((IAssociation) link.getModel()).getMemberEnds()[0];
		var quantifier = this.buildQuantifier(graph, attr);
		if (quantifier == null) {
			return;
		}

		var allPoints = this.getAllPoints();
		var len = allPoints.length;
		var x1 = allPoints[len - 1].getX();
		var x2 = allPoints[len - 2].getX();
		var y1 = allPoints[len - 1].getY();
		var y2 = allPoints[len - 2].getY();
		var geo = quantifier.getGeometry();
		var width = geo.getWidth();
		var height = geo.getHeight();
		var target = this.getTarget().getOrBuild(graph);
		var targetGeo = target.getGeometry();
		if (x1 != x2) {
			var atan = Math.atan((y2 - y1) / (x2 - x1));

			if (Math.abs(atan) > Math.toRadians(45)) {
				if (y2 > y1) {
					// 下方向に限定子がある
					geo.setX(x1 - width / 2);
					geo.setY(targetGeo.getY() + targetGeo.getHeight());
				} else {
					// 上方向に限定子がある
					geo.setX(x1 - width / 2);
					geo.setY(targetGeo.getY() - height);
				}
			} else if (x2 > x1) {
				// 右方向に限定子がある
				geo.setX(targetGeo.getX() + targetGeo.getWidth());
				geo.setY(y1 - height / 2);
			} else {
				// 左方向に限定子がある
				geo.setX(targetGeo.getX() - width);
				geo.setY(y1 - height / 2);
			}
		} else if (y2 > y1) {
			// 下方向に限定子がある
			geo.setX(y1 - width / 2);
			geo.setY(targetGeo.getY() + targetGeo.getHeight());
		} else {
			// 上方向に限定子がある
			geo.setX(x1 - width / 2);
			geo.setY(targetGeo.getY() - height);
		}

		parent.setTarget(quantifier);
		createGroup(graph, target, quantifier);
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

	private void buildEndBQuantifier(mxGraph graph, mxCell parent) {
		var link = this.getLinkPresentation();
		var attr = ((IAssociation) link.getModel()).getMemberEnds()[1];
		var quantifier = this.buildQuantifier(graph, attr);
		if (quantifier == null) {
			return;
		}

		var allPoints = this.getAllPoints();
		var x1 = allPoints[0].getX();
		var x2 = allPoints[1].getX();
		var y1 = allPoints[0].getY();
		var y2 = allPoints[1].getY();
		var geo = quantifier.getGeometry();
		var width = geo.getWidth();
		var height = geo.getHeight();
		var source = this.getSource().getOrBuild(graph);
		var sourceGeo = source.getGeometry();
		if (x1 != x2) {
			var atan = Math.atan((y2 - y1) / (x2 - x1));

			if (Math.abs(atan) > Math.toRadians(45)) {
				if (y2 > y1) {
					// 下方向に限定子がある
					geo.setX(x1 - width / 2);
					geo.setY(sourceGeo.getY() + sourceGeo.getHeight());
				} else {
					// 上方向に限定子がある
					geo.setX(x1 - width / 2);
					geo.setY(sourceGeo.getY() - height);
				}
			} else if (x2 > x1) {
				// 右方向に限定子がある
				geo.setX(sourceGeo.getX() + sourceGeo.getWidth());
				geo.setY(y1 - height / 2);
			} else {
				// 左方向に限定子がある
				geo.setX(sourceGeo.getX() - width);
				geo.setY(y1 - height / 2);
			}
		} else if (y2 > y1) {
			// 下方向に限定子がある
			geo.setX(y1 - width / 2);
			geo.setY(sourceGeo.getY() + sourceGeo.getHeight());
		} else {
			// 上方向に限定子がある
			geo.setX(x1 - width / 2);
			geo.setY(sourceGeo.getY() - height);
		}

		parent.setSource(quantifier);
		createGroup(graph, source, quantifier);
	}

}
