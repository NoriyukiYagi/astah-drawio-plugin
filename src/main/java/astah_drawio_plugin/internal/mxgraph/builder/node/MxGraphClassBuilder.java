package astah_drawio_plugin.internal.mxgraph.builder.node;

import java.util.Arrays;
import java.util.HashSet;

import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IEnumeration;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Class")
public class MxGraphClassBuilder extends MxGraphNodeBuilder {

	private static int CLASS_NAME_LINE_HEIGHT = 15;
	private static int CLASS_NAME_MARGIN = 10;
	private static int SEPARATOR_MARGIN = 4;
	private static int SEPARATOR_HEIGHT = 8;
	private static int LINE_HEIGHT = 16;

	private static String getVisibilityString(INamedElement e) {
		if (e.isPrivateVisibility()) {
			return "- ";
		} else if (e.isProtectedVisibility()) {
			return "# ";
		} else if (e.isPackageVisibility()) {
			return "~ ";
		} else {
			return "+ ";
		}
	}

	private int classNameSeparatorPosition = 0;
	private int attributesSeparatorPosition = 0;
	private int calculatedHeight = 0;

	public MxGraphClassBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.setValue(p.getLabel());

		int lines = this.getLabel().split("\n").length;
		this.classNameSeparatorPosition = CLASS_NAME_MARGIN + lines * CLASS_NAME_LINE_HEIGHT;

		var notationType = p.getProperty(PresentationPropertyConstants.Key.NOTATION_TYPE);
		var stereotypes = new HashSet<>(Arrays.asList(this.getStereotypes()));

		if ("normal".equals(notationType)) {
			if (stereotypes.contains("interface") && this.isTargetOfUsageLink()) {
				this.setStereotypesVisible(false);
				this.addStyle("html", "1");
				this.addStyle("strokeColor", "none");
				this.addStyle("fillColor", "none");
				this.addStyle("labelPosition", "center");
				this.addStyle("verticalLabelPosition", "bottom");
				this.addStyle("align", "center");
				this.addStyle("verticalAlign", "top");
				// Resize
				this.setWidth(this.getWidth() / 2);
				this.setHeight(this.getHeight() / 2);
				this.setX(this.getX() + this.getWidth() / 2);
				this.setY(this.getY() + this.getHeight() / 2);
			} else {
				this.addStyle("swimlane", null);
				this.addStyle("html", "1");
				this.addStyle("align", "center");
				this.addStyle("verticalAlign", "top");
				this.addStyle("childLayout", "stackLayout");
				this.addStyle("horizontal", "1");
				this.addStyle("startSize", String.valueOf(this.classNameSeparatorPosition));
				this.addStyle("horizontalStack", "0");
				this.addStyle("resizeParent", "1");
				this.addStyle("resizeParentMax", "0");
				this.addStyle("resizeLast", "0");
				this.addStyle("collapsible", "0");
				this.addStyle("marginBottom", "0");
				this.addStyle("whiteSpace", "wrap");
				this.addStyle("swimlaneFillColor", this.getStyle("fillColor"));
			}
		} else if (stereotypes.contains("entity")) {
			this.setStereotypesVisible(false);
			this.addStyle("ellipse", null);
			this.addStyle("html", "1");
			this.addStyle("shape", "umlEntity");
			this.addStyle("labelPosition", "center");
			this.addStyle("verticalLabelPosition", "bottom");
			this.addStyle("align", "center");
			this.addStyle("verticalAlign", "top");
		} else if (stereotypes.contains("boundary")) {
			this.setStereotypesVisible(false);
			this.addStyle("ellipse", null);
			this.addStyle("html", "1");
			this.addStyle("shape", "umlBoundary");
			this.addStyle("labelPosition", "center");
			this.addStyle("verticalLabelPosition", "bottom");
			this.addStyle("align", "center");
			this.addStyle("verticalAlign", "top");
		} else if (stereotypes.contains("control")) {
			this.setStereotypesVisible(false);
			this.addStyle("ellipse", null);
			this.addStyle("html", "1");
			this.addStyle("shape", "umlControl");
			this.addStyle("labelPosition", "center");
			this.addStyle("verticalLabelPosition", "bottom");
			this.addStyle("align", "center");
			this.addStyle("verticalAlign", "top");
		} else if (stereotypes.contains("interface")) {
			this.setStereotypesVisible(false);
			this.addStyle("ellipse", null);
			this.addStyle("html", "1");
			this.addStyle("labelPosition", "center");
			this.addStyle("verticalLabelPosition", "bottom");
			this.addStyle("align", "center");
			this.addStyle("verticalAlign", "top");
		}
	}

	private boolean isTargetOfUsageLink() {
		var p = (INodePresentation) this.getAstahPresentation();
		for (var link : p.getLinks()) {
			if ("Usage".equals(link.getType()) && link.getSource() == p) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);

		var notationType = this.getAstahPresentation().getProperty(PresentationPropertyConstants.Key.NOTATION_TYPE);
		var stereotypes = new HashSet<>(Arrays.asList(this.getStereotypes()));
		if ("normal".equals(notationType) && !this.isTargetOfUsageLink()) {
			var model = this.getAstahPresentation().getModel();
			if (model instanceof IEnumeration) {
				this.buildEnumerationLiterals(graph, parent);
			} else {
				this.buildAttributes(graph, parent);
				this.buildSeparator(graph, parent);
				this.buildOperations(graph, parent);
			}
			var geo = parent.getGeometry();
			geo.setHeight(this.calculatedHeight);
		} else if (stereotypes.contains("business")) {
			var styles = "html=1;endArrow=none;";
			var separator = (mxCell) graph.insertEdge(parent, generateId(), null, null, null, styles);
			var geo = separator.getGeometry();
			geo.setRelative(true);
			geo.setSourcePoint(new mxPoint(this.getWidth() / 2, this.getHeight()));
			geo.setTargetPoint(new mxPoint(this.getWidth(), this.getHeight() / 2));
		}
	}

	protected void buildEnumerationLiterals(mxGraph graph, mxCell parent) {
		var pos = this.classNameSeparatorPosition + SEPARATOR_MARGIN;
		var model = (IEnumeration) this.getAstahPresentation().getModel();

		for (var e : model.getEnumerationLiterals()) {
			var styles = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;spacing=0;";
			var attrText = (mxCell) graph.insertVertex(parent, generateId(), e.getName(), 0, 0, 0, 0, styles);
			var geo = attrText.getGeometry();
			geo.setRect(0, pos, this.getWidth(), LINE_HEIGHT);
			pos += LINE_HEIGHT;
		}
		this.calculatedHeight = pos;
	}

	protected void buildAttributes(mxGraph graph, mxCell parent) {
		var pos = this.classNameSeparatorPosition + SEPARATOR_MARGIN;
		var model = (IClass) this.getAstahPresentation().getModel();

		for (var attr : model.getAttributes()) {
			var association = attr.getAssociation();
			if (association != null) {
				continue;
			}
			var sb = new StringBuilder();
			sb.append(getVisibilityString(attr));
			sb.append(attr.getName());
			sb.append(" : ");
			sb.append(attr.getTypeExpression());

			var styles = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;spacing=0;";
			var attrText = (mxCell) graph.insertVertex(parent, generateId(), sb.toString(), 0, 0, 0, 0, styles);
			var geo = attrText.getGeometry();
			geo.setRect(0, pos, this.getWidth(), LINE_HEIGHT);
			pos += LINE_HEIGHT;
		}
		this.attributesSeparatorPosition = pos;
	}

	protected void buildSeparator(mxGraph graph, mxCell parent) {
		var styles = "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;";
		var separator = (mxCell) graph.insertVertex(parent, generateId(), null, 0, 0, 0, 0, styles);
		var geo = separator.getGeometry();
		geo.setRect(0, this.attributesSeparatorPosition, this.getWidth(), SEPARATOR_HEIGHT);
	}

	protected void buildOperations(mxGraph graph, mxCell parent) {
		var pos = this.attributesSeparatorPosition + SEPARATOR_HEIGHT + SEPARATOR_MARGIN;
		var model = (IClass) this.getAstahPresentation().getModel();

		for (var ope : model.getOperations()) {
			var sb = new StringBuilder();
			sb.append(getVisibilityString(ope));
			sb.append(ope.getName());
			sb.append(" : ");
			sb.append(ope.getQualifiedReturnTypeExpression());

			var styles = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;spacing=0;";
			var opeText = (mxCell) graph.insertVertex(parent, generateId(), sb.toString(), 0, 0, 0, 0, styles);
			var geo = opeText.getGeometry();
			geo.setRect(0, pos, this.getWidth(), LINE_HEIGHT);
			pos += LINE_HEIGHT;
		}
		this.calculatedHeight = pos + SEPARATOR_MARGIN;
	}
}
