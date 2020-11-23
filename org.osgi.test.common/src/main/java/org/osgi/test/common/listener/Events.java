package org.osgi.test.common.listener;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.dictionary.Dictionaries;
import org.osgi.test.common.listener.observer.BundleEventObserverMulti;
import org.osgi.test.common.listener.observer.BundleEventObserverSingle;
import org.osgi.test.common.listener.observer.FrameworkEventObserverMulti;
import org.osgi.test.common.listener.observer.FrameworkEventObserverSingle;
import org.osgi.test.common.listener.observer.Observer;
import org.osgi.test.common.listener.observer.ServiceEventObserverMulti;
import org.osgi.test.common.listener.observer.ServiceEventObserverSingle;

public class Events {

	private Events() {}

	public static Predicate<BundleEvent> isBundleEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	public static Predicate<FrameworkEvent> isFrameworkEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

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

	public static Observer<BundleEvent> newBundleEventObserver(BundleContext bundleContext,
		Predicate<BundleEvent> matches, boolean immidiate) {
		return new BundleEventObserverSingle(bundleContext, matches, immidiate);
	}

	public static Observer<List<BundleEvent>> newBundleEventObserver(BundleContext bundleContext,
		Predicate<BundleEvent> matches, int count, boolean immidiate) {
		return new BundleEventObserverMulti(bundleContext, matches, count, immidiate);
	}

	public static Observer<FrameworkEvent> newFrameworkEventObserver(BundleContext bundleContext,
		Predicate<FrameworkEvent> matches, boolean immidiate) {
		return new FrameworkEventObserverSingle(bundleContext, matches, immidiate);
	}

	public static Observer<List<FrameworkEvent>> newFrameworkEventObserver(BundleContext bundleContext,
		Predicate<FrameworkEvent> matches, int count, boolean immidiate) {
		return new FrameworkEventObserverMulti(bundleContext, matches, count, immidiate);
	}

	public static Observer<ServiceEvent> newServiceEventObserver(BundleContext bundleContext,
		Predicate<ServiceEvent> matches, boolean immidiate) {
		return new ServiceEventObserverSingle(bundleContext, matches, immidiate);
	}

	public static Observer<List<ServiceEvent>> newServiceEventObserver(BundleContext bundleContext,
		Predicate<ServiceEvent> matches, int count, boolean immidiate) {
		return new ServiceEventObserverMulti(bundleContext, matches, count, immidiate);
	}

}
