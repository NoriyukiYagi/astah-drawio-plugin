package astah_drawio_plugin.internal.mxgraph.builder.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

public class MxGraphNodeBuilderFactoryMap
		implements Iterable<Map.Entry<Class<? extends MxGraphNodeBuilder>, MxGraphNodeBuilderFactory>> {

	private HashMap<Class<? extends MxGraphNodeBuilder>, MxGraphNodeBuilderFactory> factories = new HashMap<>();

	public void put(Class<? extends MxGraphNodeBuilder> builderClass, MxGraphNodeBuilderFactory factory) {
		this.factories.put(builderClass, factory);
	}

	public void putDefault(Class<? extends MxGraphNodeBuilder> builderClass)
			throws NoSuchMethodException, SecurityException {
		this.put(builderClass, new DefaultMxGraphNodeBuilderFactory(builderClass));
	}

	public Stream<Map.Entry<Class<? extends MxGraphNodeBuilder>, MxGraphNodeBuilderFactory>> stream() {
		return this.factories.entrySet().stream();
	}

	public Set<Class<? extends MxGraphNodeBuilder>> keys() {
		return this.factories.keySet();
	}

	@Override
	public Iterator<Entry<Class<? extends MxGraphNodeBuilder>, MxGraphNodeBuilderFactory>> iterator() {
		return factories.entrySet().iterator();
	}

}
