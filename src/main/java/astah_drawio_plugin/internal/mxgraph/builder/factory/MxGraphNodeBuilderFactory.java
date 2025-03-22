package astah_drawio_plugin.internal.mxgraph.builder.factory;

import java.lang.reflect.InvocationTargetException;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@FunctionalInterface
public interface MxGraphNodeBuilderFactory {
	public MxGraphNodeBuilder create(MxGraphNodeBuilder parent, INodePresentation astahNode)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}