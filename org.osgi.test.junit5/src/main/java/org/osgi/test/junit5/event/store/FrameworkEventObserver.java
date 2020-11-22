package org.osgi.test.junit5.event.store;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public class FrameworkEventObserver extends AbstractObserver<List<FrameworkEvent>> {
	private FrameworkListener		listener;
	private EventStore				store;
	private List<FrameworkEvent>	objects	= new ArrayList<>();

	public FrameworkEventObserver(EventStore store, Predicate<FrameworkEvent> matches, int count, boolean immidiate) {
		super(count, immidiate);
		this.store = store;
		listener = new FrameworkListener() {

			@Override
			public void frameworkEvent(FrameworkEvent event) {
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
		store.removeFrameworkListenerDelegate(listener);
	}

	@Override
	protected void register() {
		store.addFrameworkListenerDelegate(listener);
	}

	@Override
	protected List<FrameworkEvent> getResultObject() {
		return objects;
	}

}
