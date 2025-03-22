package astah_drawio_plugin.internal.mxgraph.builder.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

public class DefaultMxGraphNodeBuilderFactory implements MxGraphNodeBuilderFactory {

	private Constructor<? extends MxGraphNodeBuilder> constructor;

	public DefaultMxGraphNodeBuilderFactory(Class<? extends MxGraphNodeBuilder> builderClass)
			throws NoSuchMethodException, SecurityException {
		this.constructor = builderClass.getConstructor(MxGraphNodeBuilder.class, INodePresentation.class);
	}

	@Override
	public MxGraphNodeBuilder create(MxGraphNodeBuilder parent, INodePresentation astahNode)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.constructor.newInstance(parent, astahNode);
	}

}
