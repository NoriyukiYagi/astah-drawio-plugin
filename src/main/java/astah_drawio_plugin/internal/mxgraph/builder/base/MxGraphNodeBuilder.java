package astah_drawio_plugin.internal.mxgraph.builder.base;

import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class MxGraphNodeBuilder extends MxGraphElementBuilder {

	private MxGraphNodeBuilder parent;

	private double x;
	private double y;
	private double width;
	private double height;

	public MxGraphNodeBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(p);
		this.parent = parent;
		var loc = p.getLocation();
		this.x = loc.getX();
		this.y = loc.getY();
		this.width = p.getWidth();
		this.height = p.getHeight();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getCenterX() {
		return (x + width / 2);
	}

	public double getCenterY() {
		return (y + height / 2);
	}

	public MxGraphNodeBuilder getParent() {
		return parent;
	}

	@Override
	protected mxCell build(mxGraph graph) {
		var mxGraphParent = (this.parent != null) ? this.parent.getOrBuild(graph) : graph.getDefaultParent();
		double rx;
		double ry;

		if (this.parent != null) {
			rx = this.getX() - this.parent.getX();
			ry = this.getY() - this.parent.getY();
		} else {
			rx = this.getX();
			ry = this.getY();
		}

		var vertex = (mxCell) graph.insertVertex(mxGraphParent, this.getId(), this.getLabel(), 0, 0, 0, 0,
				this.getStyles());
		var geo = vertex.getGeometry();
		geo.setRect(rx, ry, this.getWidth(), this.getHeight());

		this.buildChildren(graph, vertex);

		return vertex;
	}

	protected void buildChildren(mxGraph graph, mxCell parent) {
		// no operation
	}
	
}
