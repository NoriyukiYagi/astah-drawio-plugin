package astah_drawio_plugin.internal.mxgraph.builder.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

public class DefaultMxGraphEdgeBuilderFactory implements MxGraphEdgeBuilderFactory {

	private Constructor<? extends MxGraphEdgeBuilder> constructor;

	public DefaultMxGraphEdgeBuilderFactory(Class<? extends MxGraphEdgeBuilder> builderClass)
			throws NoSuchMethodException, SecurityException {
		this.constructor = builderClass.getConstructor(ILinkPresentation.class, MxGraphNodeBuilder.class,
				MxGraphNodeBuilder.class);
	}

	@Override
	public MxGraphEdgeBuilder create(ILinkPresentation astahLink, MxGraphNodeBuilder source, MxGraphNodeBuilder target)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.constructor.newInstance(astahLink, source, target);
	}

}
