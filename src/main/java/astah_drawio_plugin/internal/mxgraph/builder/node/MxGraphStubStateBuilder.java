package astah_drawio_plugin.internal.mxgraph.builder.node;

import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "StubState in SubmachineState")
public class MxGraphStubStateBuilder extends MxGraphNodeBuilder {

	public MxGraphStubStateBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);
		this.setValue(p.getLabel());
		this.addStyle("rounded", "0");
		this.addStyle("whiteSpace", "wrap");
		this.addStyle("html", "1");
		this.addStyle("strokeColor", "none");
		this.addStyle("fillColor", "none");
	}

	@Override
	protected void buildChildren(mxGraph graph, mxCell parent) {
		super.buildChildren(graph, parent);
		var styles = "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeWidth=3";
		var separator = (mxCell) graph.insertVertex(parent, generateId(), null, 0, 0, 0, 0, styles);
		var geo = separator.getGeometry();
		geo.setRect(0, 0, this.getWidth(), 3);
	}
}
