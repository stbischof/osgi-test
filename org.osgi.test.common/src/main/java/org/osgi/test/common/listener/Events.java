package org.osgi.test.common.listener;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
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

public class Events {

	private Events() {}

	public static Predicate<BundleEvent> isBundleEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	public static Predicate<FrameworkEvent> isFrameworkEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	public static Predicate<ServiceEvent> isServiceEventType(final int eventTypeMask) {
		return e -> (e.getType() & eventTypeMask) != 0;
	}

	public static Predicate<ServiceEvent> hasServiceObjectClass(final Class<?> objectClass) {
		return e -> containsServiceProperty(Constants.OBJECTCLASS, objectClass.getName()).test(e);
	}

	public static Predicate<ServiceEvent> containsServiceProperty(final String key, Object value) {
		return e -> containsServiceProperties(Dictionaries.dictionaryOf(key, value)).test(e);
	}

	public static Predicate<ServiceEvent> containsServiceProperties(Map<String, Object> map) {
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

	public static Predicate<ServiceEvent> containsServiceProperties(Dictionary<String, Object> dictionary) {
		return e -> containsServiceProperties(Dictionaries.asMap(dictionary)).test(e);
	}

	public static Predicate<ServiceEvent> isServiceEventWith(int eventTypeMask, final Class<?> objectClass) {
		return e -> isServiceEventType(eventTypeMask).test(e) && (hasServiceObjectClass(objectClass).test(e));
	}

	public static Predicate<ServiceEvent> isServiceEventWith(int eventTypeMask, final Class<?> objectClass,
		Map<String, Object> map) {
		return e -> isServiceEventType(eventTypeMask).test(e) && (hasServiceObjectClass(objectClass).test(e))
			&& containsServiceProperties(map).test(e);
	}

	public static Predicate<ServiceEvent> isServiceEventWith(int eventTypeMask, final Class<?> objectClass,
		Dictionary<String, Object> dictionary) {
		return e -> isServiceEventWith(eventTypeMask, objectClass, Dictionaries.asMap(dictionary)).test(e);
	}

	public static Predicate<ServiceEvent> isServiceRegistered(final Class<?> objectClass) {
		return isServiceEventWith(ServiceEvent.REGISTERED, objectClass);
	}

	public static Predicate<ServiceEvent> isServiceRegisteredWith(final Class<?> objectClass, Map<String, Object> map) {
		return isServiceEventWith(ServiceEvent.REGISTERED, objectClass, map);
	}

	public static Predicate<ServiceEvent> isServiceRegisteredWith(final Class<?> objectClass,
		Dictionary<String, Object> dictionary) {
		return isServiceEventWith(ServiceEvent.REGISTERED, objectClass, dictionary);
	}

	public static Predicate<ServiceEvent> isServiceUnregistering(final Class<?> objectClass) {
		return isServiceEventWith(ServiceEvent.UNREGISTERING, objectClass);
	}

	public static Predicate<ServiceEvent> isServiceModified(final Class<?> objectClass) {
		return isServiceEventWith(ServiceEvent.MODIFIED, objectClass);
	}

	public static Predicate<ServiceEvent> isServiceModifiedEndmatch(final Class<?> objectClass) {
		return isServiceEventWith(ServiceEvent.MODIFIED_ENDMATCH, objectClass);
	}

	/**
	 * {@link Observer} that will waitFor for a single matching Event.
	 *
	 * @param bundleContext - the Observer will add and remove a listener.
	 * @param matches - Predicate that would be tested against the Events
	 * @param immidiate - true start the Observer immediately. Could also be
	 *            started later using start or waitFor method.
	 * @return Observer<Optional<BundleEvent>>
	 */
	public static Observer<Optional<BundleEvent>> newBundleEventObserver(BundleContext bundleContext,
		Predicate<BundleEvent> matches, boolean immidiate) {
		return new BundleEventObserver.Single(bundleContext, matches, immidiate);
	}

	/**
	 * {@link Observer} that will waitFor for multiple matching Event.
	 *
	 * @param bundleContext - the Observer will add and remove a listener.
	 * @param matches - Predicate that would be tested against the Events
	 * @param count - the among of matching events the Observer is waitFor
	 * @param immidiate - true start the Observer immediately. Could also be
	 *            started later using start or waitFor method.
	 * @return Observer<List<BundleEvent>>
	 */
	public static Observer<List<BundleEvent>> newBundleEventObserver(BundleContext bundleContext,
		Predicate<BundleEvent> matches, int count, boolean immidiate) {
		return new BundleEventObserver.Multiple(bundleContext, matches, count, immidiate);
	}

	/**
	 * {@link Observer} that will waitFor for a single matching Event.
	 *
	 * @param bundleContext - the Observer will add and remove a listener.
	 * @param matches - Predicate that would be tested against the Events
	 * @param immidiate - true start the Observer immediately. Could also be
	 *            started later using start or waitFor method.
	 * @return Observer<Optional<FrameworkEvent>>
	 */

	public static Observer<Optional<FrameworkEvent>>

		newFrameworkEventObserver(BundleContext bundleContext, Predicate<FrameworkEvent> matches, boolean immidiate) {
		return new FrameworkEventObserver.Single(bundleContext, matches, immidiate);
	}

	/**
	 * {@link Observer} that will waitFor for multiple matching Event.
	 *
	 * @param bundleContext - the Observer will add and remove a listener.
	 * @param matches - Predicate that would be tested against the Events
	 * @param count - the among of matching events the Observer is waitFor
	 * @param immidiate - true start the Observer immediately. Could also be
	 *            started later using start or waitFor method.
	 * @return Observer<List<FrameworkEvent>>
	 */
	public static Observer<List<FrameworkEvent>> newFrameworkEventObserver(BundleContext bundleContext,
		Predicate<FrameworkEvent> matches, int count, boolean immidiate) {
		return new FrameworkEventObserver.Multiple(bundleContext, matches, count, immidiate);
	}

	/**
	 * {@link Observer} that will waitFor for a single matching Event.
	 *
	 * @param bundleContext - the Observer will add and remove a listener.
	 * @param matches - Predicate that would be tested against the Events
	 * @param immidiate - true start the Observer immediately. Could also be
	 *            started later using start or waitFor method.
	 * @return Observer<Optional<ServiceEvent>>
	 */

	public static Observer<Optional<ServiceEvent>> newServiceEventObserver(BundleContext bundleContext,
		Predicate<ServiceEvent> matches, boolean immidiate) {
		return new ServiceEventObserver.Single(bundleContext, matches, immidiate);
	}

	/**
	 * {@link Observer} that will waitFor for multiple matching Event.
	 *
	 * @param bundleContext - the Observer will add and remove a listener.
	 * @param matches - Predicate that would be tested against the Events
	 * @param count - the among of matching events the Observer is waitFor
	 * @param immidiate - true start the Observer immediately. Could also be
	 *            started later using start or waitFor method.
	 * @return Observer<ServiceEvent>
	 */
	public static Observer<List<ServiceEvent>> newServiceEventObserver(BundleContext bundleContext,
		Predicate<ServiceEvent> matches, int count, boolean immidiate) {
		return new ServiceEventObserver.Multiple(bundleContext, matches, count, immidiate);
	}

}
