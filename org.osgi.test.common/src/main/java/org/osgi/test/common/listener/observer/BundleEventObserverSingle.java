package org.osgi.test.common.listener.observer;

import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;

public class BundleEventObserverSingle extends AbstractBundleEventObserver<BundleEvent> {

	public BundleEventObserverSingle(BundleContext bundleContext, Predicate<BundleEvent> matches, boolean immidiate) {
		super(bundleContext, matches, 1, immidiate);
	}

	@Override
	protected BundleEvent getResultObject() {
		return objects().get(0);
	}

}
