package astah_drawio_plugin.internal.mxgraph.builder.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphEdgeBuilder;

public class MxGraphEdgeBuilderFactoryMap
		implements Iterable<Map.Entry<Class<? extends MxGraphEdgeBuilder>, MxGraphEdgeBuilderFactory>> {

	private HashMap<Class<? extends MxGraphEdgeBuilder>, MxGraphEdgeBuilderFactory> factories = new HashMap<>();

	public void put(Class<? extends MxGraphEdgeBuilder> builderClass, MxGraphEdgeBuilderFactory factory) {
		this.factories.put(builderClass, factory);
	}

	public void putDefault(Class<? extends MxGraphEdgeBuilder> builderClass)
			throws NoSuchMethodException, SecurityException {
		this.put(builderClass, new DefaultMxGraphEdgeBuilderFactory(builderClass));
	}

	public Stream<Map.Entry<Class<? extends MxGraphEdgeBuilder>, MxGraphEdgeBuilderFactory>> stream() {
		return this.factories.entrySet().stream();
	}

	@Override
	public Iterator<Entry<Class<? extends MxGraphEdgeBuilder>, MxGraphEdgeBuilderFactory>> iterator() {
		return factories.entrySet().iterator();
	}

}
