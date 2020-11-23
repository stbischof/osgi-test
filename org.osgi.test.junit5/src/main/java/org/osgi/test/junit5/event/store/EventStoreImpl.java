package org.osgi.test.junit5.event.store;

import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
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
		return getEventsService().filter(EventStore.serviceEventType(eventTypeMask));
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
	public Observer<List<BundleEvent>> newBundleEventObervator(Predicate<BundleEvent> matches, int count,
		boolean immidiate) {
		return new BundleEventObserver(this, matches, count, immidiate);
	}

	@Override
	public Observer<List<FrameworkEvent>> newFrameworkEventObervator(Predicate<FrameworkEvent> matches, int count,
		boolean immidiate) {
		return new FrameworkEventObserver(this, matches, count, immidiate);
	}

	@Override
	public Observer<List<ServiceEvent>> newServiceEventObervator(Predicate<ServiceEvent> matches, int count,
		boolean immidiate) {
		return new ServiceEventObserver(this, matches, count, immidiate);
	}

	@Override
	public Observer<Optional<ServiceReference<?>>> newSingleServiceEventObervator(Predicate<ServiceEvent> matches,
		boolean immidiate) {
		return new ServiceEventObserverSingleton(this, matches, immidiate);
	}

}
