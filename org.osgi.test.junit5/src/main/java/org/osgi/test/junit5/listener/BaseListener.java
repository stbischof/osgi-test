package org.osgi.test.junit5.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class BaseListener implements FrameworkListener, BundleListener, ServiceListener {
	public static Predicate<? super BundleEvent> isBundleEventType(final int eventType) {
		return e -> e.getType() == eventType;
	}

	public static Predicate<? super FrameworkEvent> isFrameworkEventType(final int eventType) {
		return e -> e.getType() == eventType;
	}

	public static Predicate<? super ServiceEvent> isServiceEventType(final int eventType) {
		return e -> e.getType() == eventType;
	}

	private Optional<BundleDelegate>	bundleDelegate;

	private List<EventObject>			events;

	private Optional<FrameworkDelegate>	frameworkDelegate;

	private Optional<ServiceDelegate>	serviceDelegate;

	public BaseListener(final FrameworkDelegate frameworkDelegate, final BundleDelegate bundleDelegate,
		final ServiceDelegate serviceDelegate) {
		this.frameworkDelegate = Optional.ofNullable(frameworkDelegate);
		this.bundleDelegate = Optional.ofNullable(bundleDelegate);
		this.serviceDelegate = Optional.ofNullable(serviceDelegate);
		resetEvents();
	}

	@Override
	public void bundleChanged(final BundleEvent event) {
		event(event);
		bundleDelegate.ifPresent(delegate -> delegate.bundleChanged(event));
	}

	private void event(final EventObject event) {
		events.add(event);
	}

	@Override
	public void frameworkEvent(final FrameworkEvent event) {
		event(event);
		frameworkDelegate.ifPresent(delegate -> delegate.frameworkEvent(event));
	}

	public Stream<EventObject> getEvents() {
		return events.stream();

	}

	public <E extends EventObject> Stream<E> getEvents(final Class<E> clazz) {
		return getEvents().filter(e -> clazz.isInstance(e))
			.map(e -> (clazz.cast(e)));

	}

	public Stream<BundleEvent> getEventsBundle() {
		return getEvents(BundleEvent.class);
	}

	public Stream<BundleEvent> getEventsBundle(final int eventType) {
		return getEventsBundle().filter(isBundleEventType(eventType));
	}

	public Stream<FrameworkEvent> getEventsFramework() {
		return getEvents(FrameworkEvent.class);
	}

	public Stream<FrameworkEvent> getEventsFramework(final int eventType) {
		return getEventsFramework().filter(isFrameworkEventType(eventType));
	}

	public Stream<ServiceEvent> getEventsService() {
		return getEvents(ServiceEvent.class);
	}

	public Stream<ServiceEvent> getEventsService(final int eventType) {
		return getEventsService().filter(isServiceEventType(eventType));
	}

	public Stream<ServiceEvent> getEventsService(ServiceReference<?> serviceReference, final int eventType) {
		return getEventsService().filter(isServiceReference(serviceReference));
	}

	private Predicate<ServiceEvent> isServiceReference(ServiceReference<?> serviceReference) {

		return event -> Objects.nonNull(serviceReference)
			&& Objects.equals(serviceReference, event.getServiceReference());
	}

	public void resetEvents() {
		events = Collections.synchronizedList(new ArrayList<EventObject>());
	}

	@Override
	public void serviceChanged(final ServiceEvent event) {
		event(event);
		serviceDelegate.ifPresent(delegate -> delegate.serviceChanged(event));
	}

	public void setBundleDelegate(final BundleDelegate bundleDelegate) {
		this.bundleDelegate = Optional.ofNullable(bundleDelegate);
	}

	public void setFrameworkDelegate(final FrameworkDelegate frameworkDelegate) {
		this.frameworkDelegate = Optional.ofNullable(frameworkDelegate);
	}

	public void setServiceDelegate(final ServiceDelegate serviceDelegate) {
		this.serviceDelegate = Optional.ofNullable(serviceDelegate);
	}
}
