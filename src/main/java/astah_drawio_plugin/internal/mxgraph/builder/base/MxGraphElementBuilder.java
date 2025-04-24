package astah_drawio_plugin.internal.mxgraph.builder.base;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public abstract class MxGraphElementBuilder {

	protected static String generateId() {
		var uuid = UUID.randomUUID();
		var byteBuffer = ByteBuffer.allocate(16);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());
		return Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
	}

	protected static String stylesMapToString(Map<String, String> styles) {
		var stylenames = new StringBuilder();
		var props = new StringBuilder();
		for (var entry : styles.entrySet()) {
			if (entry.getValue() == null) {
				stylenames.append(entry.getKey());
				stylenames.append(";");
			} else {
				props.append(entry.getKey());
				props.append("=");
				props.append(entry.getValue());
				props.append(";");
			}
		}
		return stylenames.toString() + props.toString();
	}

	private IPresentation astahPresentation;
	private IElement astahModel;

	private String id;
	private HashMap<String, String> styles = new HashMap<>();
	private String value;
	private List<String> stereotypes = new ArrayList<String>();
	private boolean isStereotypesVisible = true;
	private mxCell built = null;

	protected MxGraphElementBuilder(IPresentation astahPresentaion) {
		this.astahPresentation = astahPresentaion;
		this.id = generateId();
		this.astahModel = astahPresentaion.getModel();
		if (this.astahModel != null) {
			this.setStereotypes(Arrays.asList(astahModel.getStereotypes()));
		}

		var props = this.astahPresentation.getProperties();
		for (var e : props.entrySet()) {
			@SuppressWarnings("unchecked")
			var entry = (Entry<String, String>) e;
			switch (entry.getKey()) {
			case PresentationPropertyConstants.Key.FILL_COLOR:
				this.styles.put("fillColor", entry.getValue());
				break;
			case PresentationPropertyConstants.Key.FONT_COLOR:
				this.styles.put("fontColor", entry.getValue());
				break;
			case PresentationPropertyConstants.Key.LINE_COLOR:
				this.styles.put("strokeColor", entry.getValue());
				break;
			case PresentationPropertyConstants.Key.LINE_SHAPE: {
				var rounded = "0";
				switch (entry.getValue()) {
				case PresentationPropertyConstants.Value.LINE_SHAPE_LINE_RIGHT_ANGLE:
					this.styles.put("edgeStyle", "orthogonalEdgeStyle");
					break;
				case PresentationPropertyConstants.Value.LINE_SHAPE_CURVE:
					rounded = "1";
					break;
				case PresentationPropertyConstants.Value.LINE_SHAPE_CURVE_RIGHT_ANGLE:
					rounded = "1";
					this.styles.put("edgeStyle", "orthogonalEdgeStyle");
					break;
				default:
					break;
				}
				this.styles.put("rounded", rounded);
				break;
			}
			case PresentationPropertyConstants.Key.LINE_WIDTH:
				if (Integer.parseInt(entry.getValue()) > 0) {
					this.styles.put("strokeWidth", entry.getValue());
				}
				break;
			case PresentationPropertyConstants.Key.LINE_TYPE:
				switch (entry.getValue()) {
				case PresentationPropertyConstants.Value.LINE_TYPE_DASH1:
					this.styles.put("dashed", "1");
					break;
				case PresentationPropertyConstants.Value.LINE_TYPE_DASH2:
					this.styles.put("dashed", "1");
					this.styles.put("dashPattern", "4 2");
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	}

	public String getId() {
		return id;
	}

	public void addStyle(String key, String value) {
		this.styles.put(key, value);
	}

	public void removeStyle(String key) {
		this.styles.remove(key);
	}

	public String getStyle(String key) {
		return this.styles.get(key);
	}

	public Map<String, String> getStylesMap() {
		return new HashMap<String, String>(this.styles);
	}

	public String getStyles() {
		return stylesMapToString(this.styles);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getStereotypes() {
		return stereotypes;
	}

	public void setStereotypes(List<String> stereotypes) {
		this.stereotypes = stereotypes;
	}

	public boolean isStereotypesVisible() {
		return isStereotypesVisible;
	}

	public void setStereotypesVisible(boolean isStereotypesVisible) {
		this.isStereotypesVisible = isStereotypesVisible;
	}

	public String getLabel() {
		var label = new StringBuilder();
		if (this.isStereotypesVisible && this.stereotypes != null && this.stereotypes.size() > 0) {
			for (var stereotype : stereotypes) {
				label.append("&lt;&lt;");
				label.append(stereotype);
				label.append("&gt;&gt;");
				label.append("\n");
			}
		}
		if (this.getValue() != null) {
			label.append(this.getValue());
		}
		if (label.length() == 0) {
			return null;
		} else {
			return label.toString();
		}
	}

	public IPresentation getAstahPresentation() {
		return astahPresentation;
	}

	protected abstract mxCell build(mxGraph graph);

	public void buildIfNeeded(mxGraph graph) {
		if (this.built == null) {
			this.built = build(graph);
		}
	}

	public mxCell getOrBuild(mxGraph graph) {
		this.buildIfNeeded(graph);
		return this.built;
	}

	public boolean isBuilt() {
		return this.built != null;
	}

}
