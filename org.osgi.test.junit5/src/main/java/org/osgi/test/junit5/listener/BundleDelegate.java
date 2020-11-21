package org.osgi.test.junit5.listener;

import org.osgi.framework.BundleEvent;

public interface BundleDelegate {

	void bundleUpdated(BundleEvent event);

	void bundleUnresolved(BundleEvent event);

	void bundleUninstalled(BundleEvent event);

	void bundleStopping(BundleEvent event);

	void bundleStopped(BundleEvent event);

	void bundleStarting(BundleEvent event);

	void bundleStarted(BundleEvent event);

	void bundleResolved(BundleEvent event);

	void bundleLazyActivation(BundleEvent event);

	void bundleInstalled(BundleEvent event);

	default void bundleChanged(BundleEvent event) {
		switch (event.getType()) {
			case BundleEvent.INSTALLED :
				bundleInstalled(event);
			case BundleEvent.LAZY_ACTIVATION :
				bundleLazyActivation(event);
			case BundleEvent.RESOLVED :
				bundleResolved(event);
			case BundleEvent.STARTED :
				bundleStarted(event);
			case BundleEvent.STARTING :
				bundleStarting(event);
			case BundleEvent.STOPPED :
				bundleStopped(event);
			case BundleEvent.STOPPING :
				bundleStopping(event);
			case BundleEvent.UNINSTALLED :
				bundleUninstalled(event);
			case BundleEvent.UNRESOLVED :
				bundleUnresolved(event);
			case BundleEvent.UPDATED :
				bundleUpdated(event);
			default :
		}
	}

}
