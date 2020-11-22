package org.osgi.test.junit5.event.store;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class BundleEventObserver extends AbstractObserver<List<BundleEvent>> {
	private BundleListener		listener;
	private EventStore			store;
	private List<BundleEvent>	objects	= new ArrayList<>();

	public BundleEventObserver(EventStore store, Predicate<BundleEvent> matches, int count, boolean immidiate) {
		super(count, immidiate);
		this.store = store;
		listener = new BundleListener() {

			@Override
			public void bundleChanged(BundleEvent event) {
				if (matches.test(event)) {
					objects.add(event);
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
		store.removeBundleListenerDelegate(listener);
	}

	@Override
	protected void register() {
		store.addBundleListenerDelegate(listener);
	}

	@Override
	protected List<BundleEvent> getResultObject() {
		return objects;
	}

}
