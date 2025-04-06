package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.model.IUseCase;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;
import astah_drawio_plugin.internal.utils.TextGeometryCalculator;

@GraphElementBuilder(astahTypes = "UseCase")
public class MxGraphUseCaseBuilder extends MxGraphNodeBuilder {

	private static int NAME_TOP_MARGIN = 2;
	private static int SEPARATOR_MARGIN = 4;

	public MxGraphUseCaseBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.setValue(p.getLabel());
		this.setStereotypesVisible(false);
		this.addStyle("html", "1");
		this.addStyle("shape", "ellipse");
		this.addStyle("align", "center");
		this.addStyle("whiteSpace", "wrap");

		var usecase = (IUseCase) p.getModel();
		if (usecase.getExtensionPoints().length == 0) {
			this.addStyle("verticalAlign", "middle");
		} else {
			this.addStyle("verticalAlign", "top");
		}
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);

		var stereotypes = this.getStereotypes();
		if (stereotypes.contains("business")) {
			var styles = "html=1;endArrow=none;";
			var line = (mxCell) graph.insertEdge(parent, generateId(), null, null, null, styles);
			var geo = line.getGeometry();
			geo.setRelative(true);
			geo.setSourcePoint(new mxPoint(this.getWidth() / 2, this.getHeight()));
			geo.setTargetPoint(new mxPoint(this.getWidth(), this.getHeight() / 2));
		}

		var usecase = (IUseCase) this.getAstahPresentation().getModel();
		if (usecase.getExtensionPoints().length > 0) {
			var lineStyles = "html=1;endArrow=none;";
			var line = (mxCell) graph.insertEdge(parent, generateId(), null, null, null, lineStyles);
			var lineGeo = line.getGeometry();
			lineGeo.setRelative(true);
			var a = this.getWidth() / 2;
			var b = this.getHeight() / 2;
			var c = TextGeometryCalculator.getTextHeight(this.getLabel()) + NAME_TOP_MARGIN + SEPARATOR_MARGIN;
			var d = Math.sqrt(a * a * (1 - Math.pow((c - b) / b, 2)));
			var px = a - d;
			var qx = a + d;
			lineGeo.setSourcePoint(new mxPoint(px, c));
			lineGeo.setTargetPoint(new mxPoint(qx, c));

			var labelX = this.getWidth() / 8;
			var labelY = c + SEPARATOR_MARGIN;
			var labelWidth = 2 * d;
			var labelHeight = TextGeometryCalculator.getTextHeight();
			var labelStyles = "text;html=1;strokeColor=none;fillColor=none;align=left;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;whiteSpace=wrap;spacing=0;";
			var label = (mxCell) graph.insertVertex(parent, generateId(), "extension points", 0, 0, 0, 0,
					labelStyles);
			var labelGeo = label.getGeometry();
			labelGeo.setRect(labelX, labelY, labelWidth, labelHeight);
			labelY += labelHeight;

			for (var exp : usecase.getExtensionPoints()) {
				label = (mxCell) graph.insertVertex(parent, generateId(), exp.getName(), 0, 0, 0, 0,
						labelStyles);
				labelGeo = label.getGeometry();
				labelGeo.setRect(labelX, labelY, labelWidth, labelHeight);
				labelY += labelHeight;
			}
		}
	}
}
