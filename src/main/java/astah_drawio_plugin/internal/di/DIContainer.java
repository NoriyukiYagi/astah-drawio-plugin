package astah_drawio_plugin.internal.di;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DIContainer {

	private Map<Class<?>, Class<?>> registered = new HashMap<>();
	private Map<Class<?>, Object> instances = new HashMap<>();

	public DIContainer() {
	}

	public DIContainer(Class<? extends DIContainerModule> moduleClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		this.registerModule(moduleClass);
	}

	public void register(Class<?> target) {
		this.registered.put(target, target);
	}

	public <T> void register(Class<T> key, Class<? extends T> target) {
		this.registered.put(key, target);
	}

	public <T> void registerInstance(Class<T> key, T target) {
		this.instances.put(key, target);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> key)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (this.instances.containsKey(key)) {
			return (T) this.instances.get(key);
		}
		var instanceClass = this.registered.get(key);
		var constructors = instanceClass.getConstructors();
		if (constructors.length == 1) {
			var constructor = constructors[0];
			var params = constructor.getParameters();
			var paramValues = new Object[params.length];
			for (var i = 0; i < params.length; ++i) {
				var paramType = params[i].getType();
				if (paramType.equals(DIContainer.class)) {
					paramValues[i] = this;
				} else if (this.instances.containsKey(paramType) || this.registered.containsKey(paramType)) {
					paramValues[i] = this.get(paramType);
				} else {
					throw new InstantiationException("Class " + paramType.getName() + " is not registered");
				}
			}
			var instance = constructor.newInstance(paramValues);
			this.instances.put(key, instance);
			return (T) instance;
		} else {
			throw new InstantiationException(
					"Class " + key.getName() + " has multiple constructors (" + constructors.length + ")");
		}
	}

	public void registerModule(Class<? extends DIContainerModule> moduleClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		this.register(moduleClass);
		DIContainerModule module = this.get(moduleClass);
		module.configure();
	}

}
