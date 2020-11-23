package org.osgi.test.common.listener.observer;

import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;

public class FrameworkEventObserverMulti extends AbstractFrameworkEventObserver<List<FrameworkEvent>> {


	public FrameworkEventObserverMulti(BundleContext bundleContext, Predicate<FrameworkEvent> matches, int count,
		boolean immidiate) {
		super(bundleContext, matches, count, immidiate);
	}

	@Override
	protected List<FrameworkEvent> getResultObject() {
		return objects();
	}

}
