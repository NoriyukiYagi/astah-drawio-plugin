package astah_drawio_plugin.internal.mxgraph.builder.edge;

import com.change_vision.jude.api.inf.model.IERSubtypeRelationship;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Subtype")
public class MxGraphERSubtypeBuilder extends MxGraphEdgeBuilder {

	private static final double ICON_WIDTH = 24;
	private static final double ICON_OVAL_WIDTH = 20;

	public MxGraphERSubtypeBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link, source, target);
		this.setValue(null);
		this.addStyle("html", "1");
		this.addStyle("rounded", "0");
		this.addStyle("startArrow", "none");
		this.addStyle("endArrow", "none");
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);

		var model = (IERSubtypeRelationship) this.getAstahPresentation().getModel();
		var iconHeight = model.isConclusive() ? ICON_OVAL_WIDTH + 4 : ICON_OVAL_WIDTH;
		var icon = (mxCell) graph.insertVertex(parent, generateId(), null, 0, 0, ICON_WIDTH, iconHeight, "group;");
		var iconGeo = icon.getGeometry();
		iconGeo.setOffset(new mxPoint(-ICON_WIDTH / 2, -iconHeight / 2));
		iconGeo.setRelative(true);
		var ovalX = (ICON_WIDTH - ICON_OVAL_WIDTH) / 2;
		graph.insertVertex(icon, generateId(), null, ovalX, 0, ICON_OVAL_WIDTH, ICON_OVAL_WIDTH, "ellipse;");
		var lineStyle = "line;strokeWidth=1;fillColor=none;";
		graph.insertVertex(icon, generateId(), null, 0, ICON_OVAL_WIDTH, ICON_WIDTH, 1, lineStyle);
		if (model.isConclusive()) {
			graph.insertVertex(icon, generateId(), null, 0, ICON_OVAL_WIDTH + 1, ICON_WIDTH,
					iconHeight - ICON_OVAL_WIDTH - 1, "rounded=0;html=1;strokeColor=none;");
			graph.insertVertex(icon, generateId(), null, 0, iconHeight, ICON_WIDTH, 1, lineStyle);
		}
	}
}
