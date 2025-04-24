package astah_drawio_plugin.internal.mxgraph.builder.node;

import java.awt.Font;
import java.util.ArrayList;

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
import astah_drawio_plugin.internal.utils.TextGeometryCalculator;

@GraphElementBuilder(astahTypes = "Class")
public class MxGraphClassBuilder extends MxGraphNodeBuilder {

	private static final int CLASS_NAME_MARGIN = 10;
	private static final int SEPARATOR_MARGIN = 4;
	private static final int SEPARATOR_HEIGHT = 8;
	private static final int TEMPLATE_PARAM_PADDING = 2;
	private static final int TEMPLATE_PARAM_BOTTOM_MARGIN = 2;

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

	private int classNameSeparatorPosition = 0;
	private int attributesSeparatorPosition = 0;
	private int calculatedHeight = 0;

	private boolean isVisibilityVisible = true;

	// Operation Visibility
	private boolean isOperationCompartmentVisible = true;
	private boolean isPublicOperationVisible = true;
	private boolean isPackageOperationVisible = true;
	private boolean isProtectedOperationVisible = true;
	private boolean isPrivateOperationVisible = true;
	private boolean isOperationStereotypeVisible = true;
	private boolean isOperationConstraintVisible = true;
	private boolean isOperationReturnTypeVisible = true;
	private boolean isOperationParamVisible = true;
	private boolean isOperationParamTypeVisible = true;
	private boolean isOperationParamDirectionVisible = true;

	// Attr Visibility
	private boolean isAttrCompartmentVisible = true;
	private boolean isPublicAttrVisible = true;
	private boolean isPackageAttrVisible = true;
	private boolean isProtectedAttrVisible = true;
	private boolean isPrivateAttrVisible = true;
	private boolean isAttrStereotypeVisible = true;
	private boolean isAttrConstraintVisible = true;
	private boolean isAttrTypeVisible = true;
	private boolean isAttrInitialValueVisible = true;

	public MxGraphClassBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);

		this.setStereotypesVisible(Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.STEREOTYPE_VISIBILITY)));
		this.isVisibilityVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.VISIBILITY_KIND_VISIBILITY));

		// Operation Visibility
		this.isOperationCompartmentVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.OPERATION_COMPARTMENT_VISIBILITY));
		this.isPublicOperationVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PUBLIC_OPERATION));
		this.isPackageOperationVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PACKAGE_OPERATION));
		this.isProtectedOperationVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PROTECTED_OPERATION));
		this.isPrivateOperationVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PRIVATE_OPERATION));
		this.isOperationStereotypeVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.OPERATION_STEREOTYPE_VISIBILITY));
		this.isOperationConstraintVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.OPERATION_CONSTRAINT_VISIBILITY));
		this.isOperationReturnTypeVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.OPERATION_RETURN_TYPE_VISIBILITY));
		this.isOperationParamVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.OPERATION_PARAMETER_VISIBILITY));
		this.isOperationParamTypeVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.OPERATION_PARAMETER_TYPE_VISIBILITY));
		this.isOperationParamDirectionVisible = Boolean.parseBoolean(
				p.getProperty(PresentationPropertyConstants.Key.PVC_OPERATION_PARAMETER_DIRECTION_KIND_VISIBILITY));

		// Attribute Visibility
		this.isAttrCompartmentVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.ATTRIBUTE_COMPARTMENT_VISIBILITY));
		this.isPublicAttrVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PUBLIC_ATTRIBUTE));
		this.isPackageAttrVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PACKAGE_ATTRIBUTE));
		this.isProtectedAttrVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PROTECTED_ATTRIBUTE));
		this.isPrivateAttrVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.PRIVATE_ATTRIBUTE));
		this.isAttrStereotypeVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.ATTRIBUTE_STEREOTYPE_VISIBILITY));
		this.isAttrConstraintVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.ATTRIBUTE_CONSTRAINT_VISIBILITY));
		this.isAttrTypeVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.ATTRIBUTE_TYPE_VISIBILITY));
		this.isAttrInitialValueVisible = Boolean
				.parseBoolean(p.getProperty(PresentationPropertyConstants.Key.ATTRIBUTE_INITIAL_VALUE_VISIBILITY));

		this.setValue(p.getLabel());

		this.classNameSeparatorPosition = CLASS_NAME_MARGIN
				+ TextGeometryCalculator.getTextHeight(this.getLabel(), "Helvetica", 12, Font.BOLD);

		var notationType = p.getProperty(PresentationPropertyConstants.Key.NOTATION_TYPE);
		var stereotypes = this.getStereotypes();

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
		} else if (stereotypes.contains("actor")) {
			this.setStereotypesVisible(false);
			this.addStyle("shape", "umlActor");
			this.addStyle("verticalLabelPosition", "bottom");
			this.addStyle("verticalAlign", "top");
			this.addStyle("html", "1");
			// Fix aspect
			var oldWidth = this.getWidth();
			this.setWidth(this.getHeight() / 2);
			this.setX(this.getX() + (oldWidth - this.getWidth()) / 2);
		}
	}

	private String getVisibilityString(INamedElement e) {
		if (this.isVisibilityVisible) {
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
		return "";
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
		if ("normal".equals(notationType) && !this.isTargetOfUsageLink()) {
			var model = this.getAstahPresentation().getModel();
			if (model instanceof IEnumeration) {
				this.buildEnumerationLiterals(graph, parent);
			} else {
				if (this.isAttrCompartmentVisible) {
					this.buildAttributes(graph, parent);
				}
				if (this.isOperationCompartmentVisible) {
					if (this.isAttrCompartmentVisible) {
						this.buildSeparator(graph, parent);
					}
					this.buildOperations(graph, parent);
				}
			}
			if (this.getHeight() < this.calculatedHeight) {
				var geo = parent.getGeometry();
				geo.setHeight(this.calculatedHeight);
			}
			this.buildTemplateParams(graph, parent);
		} else if (this.getStereotypes().contains("business")) {
			var styles = "html=1;endArrow=none;";
			var separator = (mxCell) graph.insertEdge(parent, generateId(), null, null, null, styles);
			var geo = separator.getGeometry();
			geo.setRelative(true);
			if (this.getStereotypes().contains("actor")) {
				geo.setSourcePoint(new mxPoint(this.getWidth() / 2, this.getHeight() / 4));
				geo.setTargetPoint(new mxPoint(this.getWidth() * 3 / 4, this.getHeight() / 8));
			} else {
				geo.setSourcePoint(new mxPoint(this.getWidth() / 2, this.getHeight()));
				geo.setTargetPoint(new mxPoint(this.getWidth(), this.getHeight() / 2));
			}
		}
	}

	private void buildTemplateParams(mxGraph graph, mxCell parent) {
		IClass model = (IClass) this.getAstahPresentation().getModel();
		var templateParams = model.getTemplateParameters();
		if (templateParams.length == 0) {
			return;
		}

		var list = new ArrayList<String>();
		for (var param : templateParams) {
			var sb = new StringBuilder();
			sb.append(param.getName());
			var typeExp = param.getTypeExpression();
			if (!typeExp.isBlank()) {
				sb.append(":");
				sb.append(typeExp);
				var defaultValue = param.getDefaultValue();
				if (defaultValue != null) {
					sb.append("=");
					sb.append(defaultValue);
				}
			}
			list.add(sb.toString());
		}
		var label = String.join(", ", list);
		var width = TextGeometryCalculator.getTextWidth(label) + TEMPLATE_PARAM_PADDING * 2;
		var height = TextGeometryCalculator.getTextHeight();
		var styles = "fontStyle=0;dashed=1;html=1;whiteSpace=wrap;fillColor=%s;".formatted(this.getStyle("fillColor"));
		var templateParamsCell = (mxCell) graph.insertVertex(parent.getParent(), generateId(), label, 0, 0, 0, 0,
				styles);
		var geo = templateParamsCell.getGeometry();
		var parentGeo = parent.getGeometry();
		if (width < parentGeo.getWidth()) {
			geo.setX(parentGeo.getX() + parentGeo.getWidth() + 20 - width);
		} else {
			geo.setX(parentGeo.getX() + 20);
		}
		geo.setY(parentGeo.getY() - height / 2 - TEMPLATE_PARAM_BOTTOM_MARGIN);
		geo.setWidth(width);
		geo.setHeight(height);
		createGroup(graph, parent, templateParamsCell);
	}

	private void buildEnumerationLiterals(mxGraph graph, mxCell parent) {
		var pos = this.classNameSeparatorPosition + SEPARATOR_MARGIN;
		var model = (IEnumeration) this.getAstahPresentation().getModel();
		var lineHeight = TextGeometryCalculator.getTextHeight();

		for (var e : model.getEnumerationLiterals()) {
			var styles = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;spacing=0;";
			var attrText = (mxCell) graph.insertVertex(parent, generateId(), e.getName(), 0, 0, 0, 0, styles);
			var geo = attrText.getGeometry();
			geo.setRect(0, pos, this.getWidth(), lineHeight);
			pos += lineHeight;
		}
		this.calculatedHeight = pos;
	}

	private void buildAttributes(mxGraph graph, mxCell parent) {
		var pos = this.classNameSeparatorPosition + SEPARATOR_MARGIN;
		var model = (IClass) this.getAstahPresentation().getModel();
		var lineHeight = TextGeometryCalculator.getTextHeight();

		for (var attr : model.getAttributes()) {
			if (attr.isPublicVisibility() && this.isPublicAttrVisible
					|| attr.isPackageVisibility() && this.isPackageAttrVisible
					|| attr.isProtectedVisibility() && this.isProtectedAttrVisible
					|| attr.isPrivateVisibility() && this.isPrivateAttrVisible) {
				var association = attr.getAssociation();
				if (association != null) {
					continue;
				}
				var sb = new StringBuilder();
				sb.append(getVisibilityString(attr));
				if (this.isAttrStereotypeVisible) {
					for (var stereotype : attr.getStereotypes()) {
						sb.append("&lt;&lt;");
						sb.append(stereotype);
						sb.append("&gt;&gt;");
					}
				}
				sb.append(attr.getName());
				if (this.isAttrTypeVisible) {
					sb.append(" : ");
					sb.append(attr.getTypeExpression());
				}
				if (this.isAttrInitialValueVisible && !attr.getInitialValue().isEmpty()) {
					sb.append(" = ");
					sb.append(attr.getInitialValue());
				}
				if (this.isAttrConstraintVisible) {
					sb.append(" ");
					for (var constraint : attr.getConstraints()) {
						sb.append("{");
						sb.append(constraint.getName());
						sb.append("}");
					}
				}

				var styles = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;spacing=0;";
				var attrText = (mxCell) graph.insertVertex(parent, generateId(), sb.toString(), 0, 0, 0, 0, styles);
				var geo = attrText.getGeometry();
				geo.setRect(0, pos, this.getWidth(), lineHeight);
				pos += lineHeight;
			}
		}
		this.attributesSeparatorPosition = pos;
	}

	private void buildSeparator(mxGraph graph, mxCell parent) {
		var styles = "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;";
		var separator = (mxCell) graph.insertVertex(parent, generateId(), null, 0, 0, 0, 0, styles);
		var geo = separator.getGeometry();
		geo.setRect(0, this.attributesSeparatorPosition, this.getWidth(), SEPARATOR_HEIGHT);
	}

	private void buildOperations(mxGraph graph, mxCell parent) {
		var pos = 0;
		if (this.isAttrCompartmentVisible) {
			pos = this.attributesSeparatorPosition + SEPARATOR_HEIGHT + SEPARATOR_MARGIN;
			;
		} else {
			pos = this.classNameSeparatorPosition + SEPARATOR_MARGIN;
		}

		var model = (IClass) this.getAstahPresentation().getModel();
		var lineHeight = TextGeometryCalculator.getTextHeight();

		for (var ope : model.getOperations()) {
			if (ope.isPublicVisibility() && this.isPublicOperationVisible
					|| ope.isPackageVisibility() && this.isPackageOperationVisible
					|| ope.isProtectedVisibility() && this.isProtectedOperationVisible
					|| ope.isPrivateVisibility() && this.isPrivateOperationVisible) {

				var sb = new StringBuilder();
				sb.append(getVisibilityString(ope));
				if (this.isOperationStereotypeVisible) {
					for (var stereotype : ope.getStereotypes()) {
						sb.append("&lt;&lt;");
						sb.append(stereotype);
						sb.append("&gt;&gt;");
					}
				}
				sb.append(ope.getName());
				sb.append("(");
				if (this.isOperationParamVisible) {
					var paramStrings = new ArrayList<String>();
					for (var param : ope.getParameters()) {
						StringBuilder paramSb = new StringBuilder();
						if (this.isOperationParamDirectionVisible) {
							paramSb.append(param.getDirection());
							paramSb.append(" ");
						}
						paramSb.append(param.getName());
						if (this.isOperationParamTypeVisible) {
							paramSb.append(" : ");
							paramSb.append(param.getTypeExpression());
						}
						paramStrings.add(paramSb.toString());
					}
					sb.append(String.join(", ", paramStrings));
				}
				sb.append(")");
				if (this.isOperationReturnTypeVisible) {
					sb.append(" : ");
					sb.append(ope.getQualifiedReturnTypeExpression());
				}
				if (this.isOperationConstraintVisible) {
					sb.append(" ");
					for (var constraint : ope.getConstraints()) {
						sb.append("{");
						sb.append(constraint.getName());
						sb.append("}");
					}
				}

				var styles = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;spacing=0;";
				var opeText = (mxCell) graph.insertVertex(parent, generateId(), sb.toString(), 0, 0, 0, 0, styles);
				var geo = opeText.getGeometry();
				geo.setRect(0, pos, this.getWidth(), lineHeight);
				pos += lineHeight;
			}
		}
		this.calculatedHeight = pos + SEPARATOR_MARGIN;
	}
}
