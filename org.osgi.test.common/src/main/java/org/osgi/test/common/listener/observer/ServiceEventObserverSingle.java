package org.osgi.test.common.listener.observer;

import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;

public class ServiceEventObserverSingle extends AbstractServiceEventObserver<ServiceEvent> {

	public ServiceEventObserverSingle(BundleContext bundleContext, Predicate<ServiceEvent> matches,
		boolean immidiate) {
		super(bundleContext, matches, 1, immidiate);
	}

	@Override
	protected ServiceEvent getResultObject() {
		return objects().get(0);
	}

}
