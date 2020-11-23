package org.osgi.test.common.listener.observer;

import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;

public class FrameworkEventObserverSingle extends AbstractFrameworkEventObserver<FrameworkEvent> {


	public FrameworkEventObserverSingle(BundleContext bundleContext, Predicate<FrameworkEvent> matches,
		boolean immidiate) {
		super(bundleContext, matches, 1, immidiate);
	}

	@Override
	protected FrameworkEvent getResultObject() {
		return objects().get(0);
	}

}
