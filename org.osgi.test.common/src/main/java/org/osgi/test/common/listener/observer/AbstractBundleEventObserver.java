package org.osgi.test.common.listener.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public abstract class AbstractBundleEventObserver<E> extends AbstracEventObserver<E> {
	private BundleListener		listener;
	private List<BundleEvent>	objects	= new ArrayList<>();

	public AbstractBundleEventObserver(BundleContext bundleContext, Predicate<BundleEvent> matches, int count,
		boolean immidiate) {
		super(count, immidiate, bundleContext);

		listener = new BundleListener() {

			@Override
			public void bundleChanged(BundleEvent event) {
				if (matches.test(event)) {
					objects().add(event);
					countDownLatch().countDown();
				}
			}
		};
		if (immidiate) {
			start();
		}
	}

	@Override
	protected void unregister() {
		bundleContext().removeBundleListener(listener);
	}

	@Override
	protected void register() {
		bundleContext().addBundleListener(listener);
	}

	protected List<BundleEvent> objects() {
		return objects;
	}

}
