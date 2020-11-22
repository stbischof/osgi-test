package org.osgi.test.junit5.event.store;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class ServiceEventObserver extends AbstractObserver<List<ServiceEvent>> {
	private ServiceListener		listener;
	private EventStore			store;
	private List<ServiceEvent>	objects	= new ArrayList<>();

	public ServiceEventObserver(EventStore store, Predicate<ServiceEvent> matches, int count, boolean immidiate) {
		super(count, immidiate);
		this.store = store;
		listener = new ServiceListener() {

			@Override
			public void serviceChanged(ServiceEvent event) {
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
		store.removeServiceListenerDelegate(listener);
	}

	@Override
	protected void register() {
		store.addServiceListenerDelegate(listener);
	}

	@Override
	protected List<ServiceEvent> getResultObject() {
		return objects;
	}

}
