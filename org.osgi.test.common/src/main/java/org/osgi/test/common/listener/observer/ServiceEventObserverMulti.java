package org.osgi.test.common.listener.observer;

import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;

public class ServiceEventObserverMulti extends AbstractServiceEventObserver<List<ServiceEvent>> {

	public ServiceEventObserverMulti(BundleContext bundleContext, Predicate<ServiceEvent> matches, int count,
		boolean immidiate) {
		super(bundleContext, matches, count, immidiate);
	}

	@Override
	protected List<ServiceEvent> getResultObject() {
		return objects();
	}

}
