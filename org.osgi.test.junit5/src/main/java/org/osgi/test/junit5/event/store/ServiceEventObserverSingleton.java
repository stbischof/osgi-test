package org.osgi.test.junit5.event.store;

import java.util.Optional;
import java.util.function.Predicate;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class ServiceEventObserverSingleton extends AbstractObserver<Optional<ServiceReference<?>>> {
	private ServiceListener		listener;
	private EventStore			store;
	private ServiceReference<?>	object	= null;

	public ServiceEventObserverSingleton(EventStore store, Predicate<ServiceEvent> matches, boolean immidiate) {
		super(1, immidiate);
		this.store = store;
		listener = new ServiceListener() {

			@Override
			public void serviceChanged(ServiceEvent event) {
				if (matches.test(event)) {
					object = event.getServiceReference();
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
	protected Optional<ServiceReference<?>> getResultObject() {
		return Optional.ofNullable(object);
	}

}
