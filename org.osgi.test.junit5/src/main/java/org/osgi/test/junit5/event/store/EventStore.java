package org.osgi.test.junit5.event.store;

import java.util.Dictionary;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.dictionary.Dictionaries;

public interface EventStore {

	static Predicate<BundleEvent> isBundleEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	static Predicate<FrameworkEvent> isFrameworkEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	// TODO: more strucure and Tests
	static Predicate<ServiceEvent> serviceEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	static Predicate<ServiceEvent> serviceObjectClass(final Class<?> clazz) {
		return e -> servicePropertiesContains(Constants.OBJECTCLASS, clazz.getName()).test(e);
	}

	static Predicate<ServiceEvent> servicePropertiesContains(final String key, Object value) {
		return e -> servicePropertiesContains(Dictionaries.dictionaryOf(key, value)).test(e);
	}

	static Predicate<ServiceEvent> servicePropertiesContains(Map<String, Object> map) {
		return e -> {
			ServiceReference<?> sr = e.getServiceReference();
			List<String> keys = Stream.of(sr.getPropertyKeys())
				.collect(Collectors.toList());
			for (Entry<String, Object> entry : map.entrySet()) {
				if (!keys.contains(entry.getKey())) {
					return false;
				}
				if (!Objects.equals(sr.getProperty(entry.getKey()), entry.getValue())) {
					return false;
				}
			}
			return true;
		};
	}

	static Predicate<ServiceEvent> servicePropertiesContains(Dictionary<String, Object> dictionary) {
		return e -> servicePropertiesContains(Dictionaries.asMap(dictionary)).test(e);
	}

	static Predicate<ServiceEvent> serviceEventWith(int eventTypeMask, final Class<?> clazz) {
		return e -> serviceEventType(eventTypeMask).test(e) && (serviceObjectClass(clazz).test(e));
	}

	static Predicate<ServiceEvent> serviceEventWith(int eventTypeMask, final Class<?> clazz, Map<String, Object> map) {
		return e -> serviceEventType(eventTypeMask).test(e) && (serviceObjectClass(clazz).test(e))
			&& servicePropertiesContains(map).test(e);

	}

	static Predicate<ServiceEvent> serviceEventWith(int eventTypeMask, final Class<?> clazz,
		Dictionary<String, Object> dictionary) {
		return e -> serviceEventWith(eventTypeMask, clazz, Dictionaries.asMap(dictionary)).test(e);

	}

	static Predicate<ServiceEvent> serviceRegistered(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.REGISTERED, clazz);
	}

	static Predicate<ServiceEvent> serviceUnregistering(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.UNREGISTERING, clazz);
	}

	static Predicate<ServiceEvent> serviceModified(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.MODIFIED, clazz);
	}

	static Predicate<ServiceEvent> serviceModifiedEndmatch(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.MODIFIED_ENDMATCH, clazz);
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

	Observer<List<BundleEvent>> newBundleEventObervator(Predicate<BundleEvent> matches, int count, boolean immidiate);

	Observer<List<FrameworkEvent>> newFrameworkEventObervator(Predicate<FrameworkEvent> matches, int count,
		boolean immidiate);

	Observer<List<ServiceEvent>> newServiceEventObervator(Predicate<ServiceEvent> matches, int count,
		boolean immidiate);

	Observer<Optional<ServiceReference<?>>> newSingleServiceEventObervator(Predicate<ServiceEvent> matches,
		boolean immidiate);

}
