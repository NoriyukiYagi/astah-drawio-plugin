package astah_drawio_plugin.internal.mxgraph.builder.factory;

import java.lang.reflect.InvocationTargetException;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@FunctionalInterface
public interface MxGraphEdgeBuilderFactory {
	public MxGraphEdgeBuilder create(ILinkPresentation astahLink, MxGraphNodeBuilder source, MxGraphNodeBuilder target)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}