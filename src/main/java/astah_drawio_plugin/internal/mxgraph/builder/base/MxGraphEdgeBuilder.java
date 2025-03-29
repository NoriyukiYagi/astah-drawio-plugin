package astah_drawio_plugin.internal.mxgraph.builder.base;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

public class MxGraphEdgeBuilder extends MxGraphElementBuilder {

	private MxGraphNodeBuilder source;
	private MxGraphNodeBuilder target;
	private Point2D[] allPoints;

	private boolean showLabel = true;

	public MxGraphEdgeBuilder(ILinkPresentation link, MxGraphNodeBuilder source, MxGraphNodeBuilder target) {
		super(link);
		this.source = source;
		this.target = target;
		this.allPoints = link.getPoints();
		setValue(link.getLabel());
	}

	public MxGraphNodeBuilder getSource() {
		return this.source;
	}

	public MxGraphNodeBuilder getTarget() {
		return this.target;
	}
	
	public Point2D[] getAllPoints() {
		return allPoints;
	}

	public void setShowLabel(boolean value) {
		this.showLabel = value;
	}

	public boolean showLabel() {
		return this.showLabel;
	}

	@Override
	protected mxCell build(mxGraph graph) {
		var edgeValue = (this.showLabel) ? this.getLabel() : null;
		var edge = (mxCell) graph.insertEdge(graph.getDefaultParent(), this.getId(), edgeValue, null, null,
				this.getStyles());
		var geo = edge.getGeometry();
		geo.setSourcePoint(new mxPoint(this.allPoints[0]));
		geo.setTargetPoint(new mxPoint(this.allPoints[this.allPoints.length - 1]));
		var points = new ArrayList<mxPoint>();
		for (var i = 1; i < this.allPoints.length - 1; ++i) {
			points.add(new mxPoint(this.allPoints[i]));
		}
		geo.setPoints(points);
		if (this.source != null) {
			edge.setSource(this.source.getOrBuild(graph));
		}
		if (this.target != null) {
			edge.setTarget(this.target.getOrBuild(graph));
		}

		this.buildChildren(graph, edge);

		return edge;
	}

	protected void buildChildren(mxGraph graph, mxCell parent) {
		// no operation
	}
}
