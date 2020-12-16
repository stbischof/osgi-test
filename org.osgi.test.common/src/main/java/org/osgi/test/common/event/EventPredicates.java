/*
 * Copyright (c) OSGi Alliance (2020). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osgi.test.common.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.dictionary.Dictionaries;

public interface EventPredicates {

	static <E> Predicate<E> any() {
		return element -> true;
	}

	static <E> Predicate<Optional<E>> isPresentAnd(final Predicate<E> predicate) {
		return optional -> optional.isPresent() && predicate.test(optional.get());
	}

	static <E> Predicate<List<E>> element(int index, final Predicate<E> predicate) {
		return elements -> elements.size() > 0 && elements.size() >= index && predicate.test(elements.get(index));
	}

	static <E> Predicate<List<E>> first(final Predicate<E> predicate) {
		return elements -> element(0, predicate).test(elements);
	}

	static <E> Predicate<List<E>> last(final Predicate<E> predicate) {
		return elements -> element(elements.size() - 1, predicate).test(elements);
	}

	static <E> Predicate<List<E>> all(final Predicate<E> predicate) {
		return elements -> elements.stream()
			.allMatch(predicate);
	}

	static <E> Predicate<List<E>> any(final Predicate<E> predicate) {
		return elements -> elements.stream()
			.anyMatch(predicate);
	}

	static <E> Predicate<List<E>> none(final Predicate<E> predicate) {
		return elements -> elements.stream()
			.noneMatch(predicate);
	}

	@SafeVarargs
	static <E> Predicate<List<E>> ordered(final Predicate<E> predicate, final Predicate<E>... predicatesNext) {
		return elements -> {

			List<Predicate<E>> predicates = new ArrayList<>();
			predicates.add(predicate);
			predicates.addAll(Arrays.asList(predicatesNext));

			long skip = 0;
			for (int i = 0; i <= predicatesNext.length - 1; i++) {
				Predicate<E> predicateBefore = predicates.get(i);
				Predicate<E> predicateAfter = predicates.get(i + 1);

				Optional<E> before = elements.stream()
					.skip(skip)
					.filter(predicateBefore)
					.findFirst();
				Optional<E> after = elements.stream()
					.skip(skip)
					.filter(predicateAfter)
					.findFirst();

				if (before.isPresent() && after.isPresent()) {

					int indexBefore = elements.indexOf(before.get());
					int indexAfter = elements.indexOf(after.get());
					if (indexBefore >= indexAfter) {
						return false;
					}
					skip = indexBefore;
				} else {
					return false;
				}
			}
			return true;
		};
	}

	static <E> Predicate<List<E>> hasSize(long size, final Predicate<E> predicate) {
		return elements -> elements.stream()
			.filter(predicate)
			.count() == size;
	}

	static <E> Predicate<List<E>> hasMoreThen(long size, final Predicate<E> predicate) {
		return elements -> elements.stream()
			.filter(predicate)
			.count() > size;
	}

	static <E> Predicate<List<E>> hasLessThen(long size, final Predicate<E> predicate) {
		return elements -> elements.stream()
			.filter(predicate)
			.count() < size;
	}

	class FrameworkEvents {

		// FramworkEvents

		static Predicate<Object> isFrameworkEvent() {
			return e -> e instanceof FrameworkEvent;
		}

		static Predicate<Object> isFrameworkEventAnd(Predicate<FrameworkEvent> predicate) {
			return e -> isFrameworkEvent().test(e) && predicate.test((FrameworkEvent) e);
		}

		static Predicate<FrameworkEvent> isType(final int eventTypeMask) {
			return e -> (e.getType() & eventTypeMask) != 0;
		}

		static Predicate<FrameworkEvent> isTypeError() {
			return e -> isType(FrameworkEvent.ERROR).test(e);
		}

		static Predicate<FrameworkEvent> isTypeInfo() {
			return e -> isType(FrameworkEvent.INFO).test(e);
		}

		static Predicate<FrameworkEvent> isTypePackagesRefreshed() {
			return e -> isType(FrameworkEvent.PACKAGES_REFRESHED).test(e);
		}

		static Predicate<FrameworkEvent> isTypeStarted() {
			return e -> isType(FrameworkEvent.STARTED).test(e);
		}

		static Predicate<FrameworkEvent> isTypeStartlevelChanged() {
			return e -> isType(FrameworkEvent.STARTLEVEL_CHANGED).test(e);
		}

		static Predicate<FrameworkEvent> isTypeStopped() {
			return e -> isType(FrameworkEvent.STOPPED).test(e);
		}

		static Predicate<FrameworkEvent> isTypeStoppedBootclasspathModified() {
			return e -> isType(FrameworkEvent.STOPPED_BOOTCLASSPATH_MODIFIED).test(e);
		}

		static Predicate<FrameworkEvent> isTypeStoppedUpdate() {
			return e -> isType(FrameworkEvent.STOPPED_UPDATE).test(e);
		}

		static Predicate<FrameworkEvent> isTypeWaitTimeout() {
			return e -> isType(FrameworkEvent.WAIT_TIMEDOUT).test(e);
		}

		static Predicate<FrameworkEvent> isTypeWarning() {
			return e -> isType(FrameworkEvent.WARNING).test(e);
		}
	}

	class ServiceEvents {

		public static Predicate<Object> isServiceEvent() {
			return e -> e instanceof ServiceEvent;
		}

		public static Predicate<Object> isServiceEventAnd(Predicate<ServiceEvent> predicate) {
			return e -> isServiceEvent().test(e) && predicate.test((ServiceEvent) e);
		}

		// ServiceEvents
		public static Predicate<ServiceEvent> isType(final int eventTypeMask) {
			return e -> (e.getType() & eventTypeMask) != 0;
		}

		public static Predicate<ServiceEvent> isTypeRegistered() {
			return e -> isType(ServiceEvent.REGISTERED).test(e);
		}

		public static Predicate<ServiceEvent> isTypeModified() {
			return e -> isType(ServiceEvent.MODIFIED).test(e);
		}

		public static Predicate<ServiceEvent> isTypeModifiedEndmatch() {
			return e -> isType(ServiceEvent.MODIFIED_ENDMATCH).test(e);
		}

		public static Predicate<ServiceEvent> isTypeUnregistering() {
			return e -> isType(ServiceEvent.UNREGISTERING).test(e);
		}

		public static Predicate<ServiceEvent> hasObjectClass(final Class<?> objectClass) {

			return e -> {
				Object classes = e.getServiceReference()
					.getProperty(Constants.OBJECTCLASS);

				if (classes != null && classes instanceof String[]) {
					return Stream.of((String[]) classes)
						.filter(Objects::nonNull)
						.anyMatch(objectClass.getName()::equals);
				}
				return false;
			};

		}

		public static Predicate<ServiceEvent> containServiceProperty(final String key, Object value) {
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

		public static Predicate<ServiceEvent> matches(int eventTypeMask, final Class<?> objectClass) {
			return e -> isType(eventTypeMask).test(e) && (hasObjectClass(objectClass).test(e));
		}

		public static Predicate<ServiceEvent> matches(int eventTypeMask, final Class<?> objectClass,
			Map<String, Object> map) {
			return e -> isType(eventTypeMask).test(e) && (hasObjectClass(objectClass).test(e))
				&& containsServiceProperties(map).test(e);
		}

		public static Predicate<ServiceEvent> matches(int eventTypeMask, final Class<?> objectClass,
			Dictionary<String, Object> dictionary) {
			return e -> matches(eventTypeMask, objectClass, Dictionaries.asMap(dictionary)).test(e);
		}

		public static Predicate<ServiceEvent> isTypeRegistered(final Class<?> objectClass) {
			return matches(ServiceEvent.REGISTERED, objectClass);
		}

		public static Predicate<ServiceEvent> isTypeRegisteredWith(final Class<?> objectClass,
			Map<String, Object> map) {
			return matches(ServiceEvent.REGISTERED, objectClass, map);
		}

		public static Predicate<ServiceEvent> isTypeRegisteredWith(final Class<?> objectClass,
			Dictionary<String, Object> dictionary) {
			return matches(ServiceEvent.REGISTERED, objectClass, dictionary);
		}

		public static Predicate<ServiceEvent> isTypeUnregistering(final Class<?> objectClass) {
			return matches(ServiceEvent.UNREGISTERING, objectClass);
		}

		public static Predicate<ServiceEvent> isTypeModified(final Class<?> objectClass) {
			return matches(ServiceEvent.MODIFIED, objectClass);
		}

		public static Predicate<ServiceEvent> isTypeModifiedEndmatch(final Class<?> objectClass) {
			return matches(ServiceEvent.MODIFIED_ENDMATCH, objectClass);
		}
	}

	interface BundleEvents {

		// BundleEvents

		static Predicate<Object> isBundleEvent() {
			return e -> e instanceof BundleEvent;
		}

		static Predicate<Object> isBundleEventAnd(Predicate<BundleEvent> predicate) {
			return e -> isBundleEvent().test(e) && predicate.test((BundleEvent) e);
		}

		static Predicate<BundleEvent> isType(final int eventTypeMask) {
			return e -> (e.getType() & eventTypeMask) != 0;
		}

		// BundleEvents - by type
		static Predicate<BundleEvent> isTypeInstalled() {
			return e -> isType(BundleEvent.INSTALLED).test(e);
		}

		static Predicate<BundleEvent> isTypeLazyActivation() {
			return e -> isType(BundleEvent.LAZY_ACTIVATION).test(e);
		}

		static Predicate<BundleEvent> isTypeResolved() {
			return e -> isType(BundleEvent.RESOLVED).test(e);
		}

		static Predicate<BundleEvent> isTypeStarted() {
			return e -> isType(BundleEvent.STARTED).test(e);
		}

		static Predicate<BundleEvent> isTypeStarting() {
			return e -> isType(BundleEvent.STARTING).test(e);
		}

		static Predicate<BundleEvent> isTypeStopped() {
			return e -> isType(BundleEvent.STOPPED).test(e);
		}

		static Predicate<BundleEvent> isTypeStopping() {
			return e -> isType(BundleEvent.STOPPING).test(e);
		}

		static Predicate<BundleEvent> isTypeUninstalled() {
			return e -> isType(BundleEvent.UNINSTALLED).test(e);
		}

		static Predicate<BundleEvent> isTypeUnresolved() {
			return e -> isType(BundleEvent.UNRESOLVED).test(e);
		}

		static Predicate<BundleEvent> isTypeUpdated() {
			return e -> isType(BundleEvent.UPDATED).test(e);
		}
	}

}
