package astah_drawio_plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import astah_drawio_plugin.internal.AppModule;
import astah_drawio_plugin.internal.ServiceLocator;
import astah_drawio_plugin.internal.di.DIContainer;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		if (ServiceLocator.getContainer() == null) {
			DIContainer container = new DIContainer(AppModule.class);
			ServiceLocator.setContainer(container);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// no operation
	}

}
