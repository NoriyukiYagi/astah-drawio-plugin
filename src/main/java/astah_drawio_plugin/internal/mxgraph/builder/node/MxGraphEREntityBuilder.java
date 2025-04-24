package astah_drawio_plugin.internal.mxgraph.builder.node;

import java.util.Map;

import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;
import astah_drawio_plugin.internal.utils.TextGeometryCalculator;

@GraphElementBuilder(astahTypes = "EREntity")
public class MxGraphEREntityBuilder extends MxGraphNodeBuilder {

	private static final int LABEL_MARGIN = 2;
	private static final int KEYS_TOP_MARGIN = 4;
	private static final int KEYS_LEFT_MARGIN = 4;
	private static final int KEYS_ITEMS_MARGIN = 8;
	private static final int SEPARATOR_HEIGHT = 8;
	private static final String FK_LABEL = "(FK)";
	private static final int FK_WIDTH = TextGeometryCalculator.getTextWidth(FK_LABEL);
	private static final String NOT_NULL_LABEL = "NOT NULL";
	private static final int NOT_NULL_WIDTH = TextGeometryCalculator.getTextWidth(NOT_NULL_LABEL);

	private Map<String, String> baseStylesMap;
	private boolean isAttributeTypeVisible;
	private boolean isNotNullVisible;
	private boolean isFkVisible;
	private String displayLevel;

	public MxGraphEREntityBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);

		this.isAttributeTypeVisible = Boolean
				.valueOf(p.getProperty(PresentationPropertyConstants.Key.TYPE_AND_LENGTH_VISIBILITY));
		this.isNotNullVisible = Boolean
				.valueOf(p.getProperty(PresentationPropertyConstants.Key.NULL_OPTION_VISIBILITY));
		this.isFkVisible = Boolean
				.valueOf(p.getProperty(PresentationPropertyConstants.Key.FOREIGN_KEY_SIGN_VISIBILITY));
		this.displayLevel = p.getProperty("er_displaylevel");

		this.baseStylesMap = this.getStylesMap();
		this.setValue(p.getLabel());

		if ("entity".equals(this.displayLevel)) {
			this.addStyle("strokeWidth", "2");
		} else {
			this.addStyle("rounded", "0");
			this.addStyle("whiteSpace", "wrap");
			this.addStyle("html", "1");
			this.addStyle("strokeColor", "none");
			this.addStyle("fillColor", "none");
			this.addStyle("verticalAlign", "top");
			this.addStyle("align", "left");
			this.addStyle("spacing", "0");
			this.addStyle("spacingTop", "-2");
			this.addStyle("fontStyle", "1");
		}
	}

	private int calcAttrNameWidth() {
		var model = (IEREntity) this.getAstahPresentation().getModel();
		int max = 0;
		for (var attr : model.getPrimaryKeys()) {
			var width = TextGeometryCalculator.getTextWidth(attr.getName());
			if (width > max) {
				max = width;
			}
		}
		for (var attr : model.getNonPrimaryKeys()) {
			var width = TextGeometryCalculator.getTextWidth(attr.getName());
			if (width > max) {
				max = width;
			}
		}
		return max;
	}

	private String getAttrTypeString(IERAttribute attr) {
		if (attr.getLengthPrecision().isEmpty()) {
			return attr.getDatatype().getName();
		} else {
			return "%s(%s)".formatted(attr.getDatatype().getName(), attr.getLengthPrecision());
		}
	}

	private int calcAttrTypeWidth() {
		if (!this.isAttributeTypeVisible) {
			return 0;
		}
		var model = (IEREntity) this.getAstahPresentation().getModel();
		int max = 0;
		for (var attr : model.getPrimaryKeys()) {
			var width = TextGeometryCalculator.getTextWidth(this.getAttrTypeString(attr));
			if (width > max) {
				max = width;
			}
		}
		for (var attr : model.getNonPrimaryKeys()) {
			var width = TextGeometryCalculator.getTextWidth(this.getAttrTypeString(attr));
			if (width > max) {
				max = width;
			}
		}
		return max;
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);

		if ("entity".equals(this.displayLevel)) {
			return;
		}

		this.baseStylesMap.put("strokeWidth", "2");
		var labelHeight = TextGeometryCalculator.getTextHeight(this.getLabel()) + LABEL_MARGIN;
		var lineHeight = TextGeometryCalculator.getTextHeight();
		var attrNameWidth = this.calcAttrNameWidth();
		var attrTypeWidth = this.calcAttrTypeWidth();
		var rect = (mxCell) graph.insertVertex(parent, generateId(), null, 0, 0, 0, 0,
				stylesMapToString(this.baseStylesMap));
		var geo = rect.getGeometry();
		geo.setRect(0, labelHeight, this.getWidth(), this.getHeight() - labelHeight);
		var y = labelHeight + KEYS_TOP_MARGIN;
		y = this.buildPrimaryKeys(graph, parent, y, lineHeight, attrNameWidth, attrTypeWidth);

		if ("primarykey".equals(this.displayLevel)) {
			if (y > this.getHeight()) {
				this.setHeight(y);
			}
			return;
		}

		y = this.buildSeparator(graph, parent, y);
		y = this.buildNonPrimaryKeys(graph, parent, y, lineHeight, attrNameWidth, attrTypeWidth);
		if (y > this.getHeight()) {
			this.setHeight(y);
		}
	}

	private int buildPrimaryKeys(mxGraph graph, mxCell parent, int y, int lineHeight, int attrNameWidth,
			int attrTypeWidth) {
		var model = (IEREntity) this.getAstahPresentation().getModel();

		for (var attr : model.getPrimaryKeys()) {
			this.buildAttrKey(graph, parent, attr, y, lineHeight, attrNameWidth, attrTypeWidth);
			y += lineHeight;
		}

		return y;
	}

	private int buildSeparator(mxGraph graph, mxCell parent, int y) {
		var styles = "line;strokeWidth=1;fillColor=none;";
		graph.insertVertex(parent, generateId(), null, 0, y, this.getWidth(), SEPARATOR_HEIGHT, styles);
		return y + SEPARATOR_HEIGHT;
	}

	private int buildNonPrimaryKeys(mxGraph graph, mxCell parent, int y, int lineHeight, int attrNameWidth,
			int attrTypeWidth) {
		var model = (IEREntity) this.getAstahPresentation().getModel();

		for (var attr : model.getNonPrimaryKeys()) {
			this.buildAttrKey(graph, parent, attr, y, lineHeight, attrNameWidth, attrTypeWidth);
			y += lineHeight;
		}

		return y;
	}

	private void buildAttrKey(mxGraph graph, mxCell parent, IERAttribute attr, int y, int lineHeight, int attrNameWidth,
			int attrTypeWidth) {
		var styles = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;";
		var x = KEYS_LEFT_MARGIN;
		graph.insertVertex(parent, generateId(), attr.getName(), x, y, attrNameWidth, lineHeight,
				styles);
		x += attrNameWidth + KEYS_ITEMS_MARGIN;

		if (this.isAttributeTypeVisible) {
			graph.insertVertex(parent, generateId(), this.getAttrTypeString(attr), x, y, attrTypeWidth, lineHeight,
					styles);
			x += attrTypeWidth + KEYS_ITEMS_MARGIN;
		}

		if (this.isNotNullVisible && attr.isNotNull()) {
			graph.insertVertex(parent, generateId(), "NOT NULL", x, y, NOT_NULL_WIDTH, lineHeight, styles);
			x += NOT_NULL_WIDTH + KEYS_ITEMS_MARGIN;
		}

		if (this.isFkVisible && attr.isForeignKey()) {
			graph.insertVertex(parent, generateId(), FK_LABEL, x, y, FK_WIDTH, lineHeight, styles);
		}
	}
}
