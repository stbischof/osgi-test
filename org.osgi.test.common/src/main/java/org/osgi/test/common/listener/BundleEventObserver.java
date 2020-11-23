package org.osgi.test.common.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public abstract class BundleEventObserver<E> extends AbstracEventObserver<E> {
	private BundleListener		listener;
	private List<BundleEvent>	objects	= new ArrayList<>();

	public BundleEventObserver(BundleContext bundleContext, Predicate<BundleEvent> matches, int count,
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

	public static class Single extends BundleEventObserver<BundleEvent> {

		public Single(BundleContext bundleContext, Predicate<BundleEvent> matches, boolean immidiate) {
			super(bundleContext, matches, 1, immidiate);
		}

		@Override
		protected BundleEvent getResultObject() {
			return objects().get(0);
		}
	}

	public static class Multiple extends BundleEventObserver<List<BundleEvent>> {

		public Multiple(BundleContext bundleContext, Predicate<BundleEvent> matches, int count, boolean immidiate) {
			super(bundleContext, matches, count, immidiate);
		}

		@Override
		protected List<BundleEvent> getResultObject() {
			return objects();
		}
	}

}
