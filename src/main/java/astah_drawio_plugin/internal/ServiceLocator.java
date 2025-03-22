package astah_drawio_plugin.internal;

import astah_drawio_plugin.internal.di.DIContainer;

public abstract class ServiceLocator {

	private static DIContainer container;

	private ServiceLocator() {
	}

	public static DIContainer getContainer() {
		return container;
	}

	public static void setContainer(DIContainer container) {
		ServiceLocator.container = container;
	}
}
