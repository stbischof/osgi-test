package org.osgi.test.junit5.event;

import java.util.EventObject;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public interface EventStore {

	static Predicate<? super BundleEvent> isBundleEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	static Predicate<? super FrameworkEvent> isFrameworkEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	static Predicate<? super ServiceEvent> isServiceEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	static <T> T getLastEvent(Stream<T> stream) {
		long count = stream.count();
		return stream.skip(count - 1)
			.findFirst()
			.get();
	}

	Stream<EventObject> getEvents();

	Stream<BundleEvent> getEventsBundle();

	Stream<BundleEvent> getEventsBundle(int eventType);

	Stream<FrameworkEvent> getEventsFramework();

	Stream<FrameworkEvent> getEventsFramework(int eventType);

	Stream<ServiceEvent> getEventsService();

	Stream<ServiceEvent> getEventsService(int eventType);

	Stream<ServiceEvent> getEventsService(ServiceReference<?> serviceReference, int eventType);

	void resetEvents();

	boolean addBundleListenerDelegate(BundleListener delegate);

	boolean addFrameworkListenerDelegate(FrameworkListener delegate);

	boolean addServiceListenerDelegate(ServiceListener delegate);

	boolean removeBundleListenerDelegate(BundleListener delegate);

	boolean removeFrameworkListenerDelegate(FrameworkListener delegate);

	boolean removeServiceListenerDelegate(ServiceListener delegate);

	EventObservator<List<BundleEvent>> newBundleEventObervator(Predicate<BundleEvent> matches, int count);

	EventObservator<List<FrameworkEvent>> newFrameworkEventObervator(Predicate<FrameworkEvent> matches, int count);

	EventObservator<List<ServiceEvent>> newServiceEventObervator(Predicate<ServiceEvent> matches, int count);

}
