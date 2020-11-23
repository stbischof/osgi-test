package org.osgi.test.common.listener;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.dictionary.Dictionaries;

public class Events {

	private Events() {}

	public static Predicate<BundleEvent> isBundleEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	public static Predicate<FrameworkEvent> isFrameworkEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	// TODO: more strucure and Tests
	public static Predicate<ServiceEvent> serviceEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	public static Predicate<ServiceEvent> serviceObjectClass(final Class<?> clazz) {
		return e -> servicePropertiesContains(Constants.OBJECTCLASS, clazz.getName()).test(e);
	}

	public static Predicate<ServiceEvent> servicePropertiesContains(final String key, Object value) {
		return e -> servicePropertiesContains(Dictionaries.dictionaryOf(key, value)).test(e);
	}

	public static Predicate<ServiceEvent> servicePropertiesContains(Map<String, Object> map) {
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

	public static Predicate<ServiceEvent> servicePropertiesContains(Dictionary<String, Object> dictionary) {
		return e -> servicePropertiesContains(Dictionaries.asMap(dictionary)).test(e);
	}

	public static Predicate<ServiceEvent> serviceEventWith(int eventTypeMask, final Class<?> clazz) {
		return e -> serviceEventType(eventTypeMask).test(e) && (serviceObjectClass(clazz).test(e));
	}

	public static Predicate<ServiceEvent> serviceEventWith(int eventTypeMask, final Class<?> clazz,
		Map<String, Object> map) {
		return e -> serviceEventType(eventTypeMask).test(e) && (serviceObjectClass(clazz).test(e))
			&& servicePropertiesContains(map).test(e);

	}

	public static Predicate<ServiceEvent> serviceEventWith(int eventTypeMask, final Class<?> clazz,
		Dictionary<String, Object> dictionary) {
		return e -> serviceEventWith(eventTypeMask, clazz, Dictionaries.asMap(dictionary)).test(e);

	}

	public static Predicate<ServiceEvent> serviceRegistered(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.REGISTERED, clazz);
	}

	public static Predicate<ServiceEvent> serviceUnregistering(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.UNREGISTERING, clazz);
	}

	public static Predicate<ServiceEvent> serviceModified(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.MODIFIED, clazz);
	}

	public static Predicate<ServiceEvent> serviceModifiedEndmatch(final Class<?> clazz) {
		return serviceEventWith(ServiceEvent.MODIFIED_ENDMATCH, clazz);
	}

}
