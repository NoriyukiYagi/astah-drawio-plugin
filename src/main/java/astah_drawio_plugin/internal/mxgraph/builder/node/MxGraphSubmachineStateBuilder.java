package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;
import astah_drawio_plugin.internal.utils.TextGeometryCalculator;

@GraphElementBuilder(astahTypes = "SubmachineState")
public class MxGraphSubmachineStateBuilder extends MxGraphNodeBuilder {

	private static int SEPARATOR_HEIGHT = 8;
	private static int MAX_ARCSIZE = 10;

	public MxGraphSubmachineStateBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.setValue(p.getLabel());
		var arcSize = Math.min(Math.min(getWidth(), getHeight()) / 5, MAX_ARCSIZE);
		this.addStyle("shape", "umlState");
		this.addStyle("rounded", "1");
		this.addStyle("verticalAlign", "top");
		this.addStyle("spacing", "0");
		this.addStyle("umlStateSymbol", "collapseState");
		this.addStyle("absoluteArcSize", "1");
		this.addStyle("arcSize", String.valueOf(arcSize));
		this.addStyle("whiteSpace", "wrap");
		this.addStyle("html", "1");
		this.addStyle("fontStyle", "1");
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);
		var labelHeight = TextGeometryCalculator.getTextHeight(this.getLabel());
		var styles = "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;";
		var separator = (mxCell) graph.insertVertex(parent, generateId(), null, 0, 0, 0, 0, styles);
		var geo = separator.getGeometry();
		geo.setRect(0, labelHeight, this.getWidth(), SEPARATOR_HEIGHT);
	}
}
