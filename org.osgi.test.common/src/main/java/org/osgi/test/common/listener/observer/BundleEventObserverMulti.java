package org.osgi.test.common.listener.observer;

import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;

public class BundleEventObserverMulti extends AbstractBundleEventObserver<List<BundleEvent>> {

	public BundleEventObserverMulti(BundleContext bundleContext, Predicate<BundleEvent> matches, int count,
		boolean immidiate) {
		super(bundleContext, matches, count, immidiate);
	}

	@Override
	protected List<BundleEvent> getResultObject() {
		return objects();
	}

}
