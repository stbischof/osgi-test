package org.osgi.test.junit5.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class EventStoreImpl implements FrameworkListener, BundleListener, ServiceListener, EventStore {

	private List<BundleListener>	bundleListenerDelegate		= Collections
		.synchronizedList(new CopyOnWriteArrayList<>());
	private List<EventObject>		events;
	private List<FrameworkListener>	frameworkListenerDelegate	= Collections
		.synchronizedList(new CopyOnWriteArrayList<>());
	private List<ServiceListener>	serviceListenerDelegate		= Collections
		.synchronizedList(new CopyOnWriteArrayList<>());

	public EventStoreImpl() {
		resetEvents();
	}

	private void event(final EventObject event) {
		events.add(event);
	}

	@Override
	public void bundleChanged(final BundleEvent event) {
		event(event);
		bundleListenerDelegate.stream()
			.forEach(delegate -> delegate.bundleChanged(event));
	}

	@Override
	public void frameworkEvent(final FrameworkEvent event) {
		event(event);
		frameworkListenerDelegate.stream()
			.forEach(delegate -> delegate.frameworkEvent(event));
	}

	@Override
	public void serviceChanged(final ServiceEvent event) {
		event(event);
		serviceListenerDelegate.stream()
			.forEach(delegate -> delegate.serviceChanged(event));
	}

	@Override
	public Stream<EventObject> getEvents() {
		return events.stream();
	}

	public <E extends EventObject> Stream<E> getEvents(final Class<E> clazz) {
		return getEvents().filter(e -> clazz.isInstance(e))
			.map(e -> (clazz.cast(e)));
	}

	@Override
	public Stream<BundleEvent> getEventsBundle() {
		return getEvents(BundleEvent.class);
	}

	@Override
	public Stream<BundleEvent> getEventsBundle(final int eventTypeMask) {
		return getEventsBundle().filter(EventStore.isBundleEventType(eventTypeMask));
	}

	@Override
	public Stream<FrameworkEvent> getEventsFramework() {
		return getEvents(FrameworkEvent.class);
	}

	@Override
	public Stream<FrameworkEvent> getEventsFramework(final int eventTypeMask) {
		return getEventsFramework().filter(EventStore.isFrameworkEventType(eventTypeMask));
	}

	@Override
	public Stream<ServiceEvent> getEventsService() {
		return getEvents(ServiceEvent.class);
	}

	@Override
	public Stream<ServiceEvent> getEventsService(final int eventTypeMask) {
		return getEventsService().filter(EventStore.isServiceEventType(eventTypeMask));
	}

	@Override
	public Stream<ServiceEvent> getEventsService(ServiceReference<?> serviceReference, final int eventTypeMask) {
		return getEventsService().filter(isServiceReference(serviceReference));
	}

	private Predicate<ServiceEvent> isServiceReference(ServiceReference<?> serviceReference) {
		return event -> Objects.nonNull(serviceReference)
			&& Objects.equals(serviceReference, event.getServiceReference());
	}

	@Override
	public void resetEvents() {
		events = Collections.synchronizedList(new CopyOnWriteArrayList<EventObject>());
	}

	@Override
	public boolean addBundleListenerDelegate(final BundleListener delegate) {
		return this.bundleListenerDelegate.add(delegate);
	}

	@Override
	public boolean addFrameworkListenerDelegate(final FrameworkListener delegate) {
		return this.frameworkListenerDelegate.add(delegate);
	}

	@Override
	public boolean addServiceListenerDelegate(final ServiceListener delegate) {
		return this.serviceListenerDelegate.add(delegate);
	}

	@Override
	public boolean removeBundleListenerDelegate(final BundleListener delegate) {
		return this.bundleListenerDelegate.remove(delegate);
	}

	@Override
	public boolean removeFrameworkListenerDelegate(final FrameworkListener delegate) {
		return this.frameworkListenerDelegate.remove(delegate);
	}

	@Override
	public boolean removeServiceListenerDelegate(final ServiceListener delegate) {
		return this.serviceListenerDelegate.remove(delegate);
	}

	@Override
	public EventObservator<List<BundleEvent>> newBundleEventObervator(Predicate<BundleEvent> matches, int count) {
		CountDownLatch conditionLatch = new CountDownLatch(count);
		List<BundleEvent> events = Collections.synchronizedList(new ArrayList<>());
		BundleListener listener = new BundleListener() {

			@Override
			public void bundleChanged(BundleEvent event) {
				if (matches.test(event)) {
					events.add(event);
					conditionLatch.countDown();
				}
			}
		};
		addBundleListenerDelegate(listener);
		return new AbstractObservator<List<BundleEvent>>() {

			@Override
			protected List<BundleEvent> get() {
				return events;
			}

			@Override
			protected void cleanUp() {
				removeBundleListenerDelegate(listener);
			}

			@Override
			CountDownLatch countDownLatch() {
				return conditionLatch;
			}
		};
	}

	@Override
	public EventObservator<List<FrameworkEvent>> newFrameworkEventObervator(Predicate<FrameworkEvent> matches,
		int count) {
		CountDownLatch conditionLatch = new CountDownLatch(count);
		List<FrameworkEvent> events = Collections.synchronizedList(new ArrayList<>());
		FrameworkListener listener = new FrameworkListener() {

			@Override
			public void frameworkEvent(FrameworkEvent event) {
				if (matches.test(event)) {
					events.add(event);
					conditionLatch.countDown();
				}
			}
		};

		addFrameworkListenerDelegate(listener);
		return new AbstractObservator<List<FrameworkEvent>>() {

			@Override
			protected List<FrameworkEvent> get() {
				return events;
			}

			@Override
			protected void cleanUp() {
				removeFrameworkListenerDelegate(listener);
			}

			@Override
			CountDownLatch countDownLatch() {
				return conditionLatch;
			}
		};
	}

	@Override
	public EventObservator<List<ServiceEvent>> newServiceEventObervator(Predicate<ServiceEvent> matches, int count) {
		CountDownLatch conditionLatch = new CountDownLatch(count);
		List<ServiceEvent> events = Collections.synchronizedList(new ArrayList<>());
		ServiceListener listener = new ServiceListener() {

			@Override
			public void serviceChanged(ServiceEvent event) {
				if (matches.test(event)) {
					events.add(event);
					conditionLatch.countDown();
				}

			}
		};

		addServiceListenerDelegate(listener);
		return new AbstractObservator<List<ServiceEvent>>() {

			@Override
			protected List<ServiceEvent> get() {
				return events;
			}

			@Override
			protected void cleanUp() {
				removeServiceListenerDelegate(listener);
			}

			@Override
			CountDownLatch countDownLatch() {
				return conditionLatch;
			}
		};
	}

}
