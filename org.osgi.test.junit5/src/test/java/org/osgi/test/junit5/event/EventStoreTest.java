/*
 * Copyright (c) OSGi Alliance (2019, 2020). All Rights Reserved.
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

package org.osgi.test.junit5.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectEventListener;
import org.osgi.test.common.annotation.InjectInstallBundle;
import org.osgi.test.common.dictionary.Dictionaries;
import org.osgi.test.common.install.InstallBundle;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.event.store.EventStore;
import org.osgi.test.junit5.event.store.Observer;
import org.osgi.test.junit5.event.store.Observer.Result;
import org.osgi.test.junit5.event.store.ServiceReferenceObservator.ServiceReferenceData;
import org.osgi.util.tracker.ServiceTracker;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(EventListenerExtension.class)
public class EventStoreTest {

	@InjectEventListener
	static EventStore	staticEventStore;

	@InjectEventListener
	EventStore			eventStore;

	@Test
	public void testObservatorBundle(@InjectEventListener EventStore eventStore, @InjectInstallBundle InstallBundle ib)
		throws Exception {

		// All clear
		assertThat(eventStore).isNotNull();
		assertThat(eventStore.getEvents()
			.count()).isEqualTo(0);

		// Test any Match on SymbolicName and INSTALL-Event
		Predicate<BundleEvent> matchesAnyInstalled = (event) -> (event.getBundle()
			.getSymbolicName()
			.matches(".*") && (event.getType() & BundleEvent.INSTALLED) != 0);

		// Create an Obervator with the given count and Predicate-Matcher
		Observer<List<BundleEvent>> eventObservator1 = eventStore.newBundleEventObervator(matchesAnyInstalled, 1, true);
		Observer<List<BundleEvent>> eventObservator2 = eventStore.newBundleEventObervator(matchesAnyInstalled, 2, true);

		// installe a Bundle to get an INSTALL-Event
		Bundle b = ib.installBundle("tb1.jar", false);

		// Wait for the event
		Result<List<BundleEvent>> result1 = eventObservator1.waitFor(2000, TimeUnit.MILLISECONDS);
		Result<List<BundleEvent>> result2 = eventObservator2.waitFor(0, TimeUnit.MILLISECONDS);

		// Check that the event happened
		assertThat(result1).isNotNull();
		assertThat(result1.isTimedOut()).isFalse();
		assertThat(result2).isNotNull();
		assertThat(result2.isTimedOut()).isTrue();

		// Check Eventcount from Result and Stream
		List<BundleEvent> observatorResultEvents1 = result1.get();
		List<BundleEvent> observatorResultEvents2 = result1.get();
		assertThat(eventStore.getEvents()).hasSize(1)
			.containsAll(observatorResultEvents1)
			.containsAll(observatorResultEvents2);

		// reset
		eventStore.resetEvents();

		// is reseted?
		assertThat(eventStore.getEvents()
			.count()).isEqualTo(0);

	}

	@Test
	public void testObservatorService(@InjectEventListener EventStore eventStore,
		@InjectBundleContext BundleContext bundleContext) throws Exception {

		// All clear
		assertThat(eventStore).isNotNull();
		assertThat(eventStore.getEvents()
			.count()).isEqualTo(0);

		// Test any REGISTERED-Event
		Predicate<ServiceEvent> matchesAnyRegistered = (event) -> (event.getType() == ServiceEvent.REGISTERED);

		// Create an Obervator with the given count and Predicate-Matcher
		Observer<List<ServiceEvent>> eventObservator1 = eventStore.newServiceEventObervator(matchesAnyRegistered, 1,
			true);

		// register a Service to get an Event
		ServiceRegistration<A> reg = bundleContext.registerService(A.class, new A() {}, Dictionaries.dictionaryOf());

		// Wait for the event
		Result<List<ServiceEvent>> result1 = eventObservator1.waitFor(2000, TimeUnit.MILLISECONDS);

		// Check that the event happened
		assertThat(result1).isNotNull();
		assertThat(result1.isTimedOut()).isFalse();

		// Check Event-count from Result and Stream
		List<ServiceEvent> observatorResultEvents1 = result1.get();

		assertThat(eventStore.getEvents()).hasSize(1)
			.containsAll(observatorResultEvents1);
		// reset
		eventStore.resetEvents();

		// is reseted?
		assertThat(eventStore.getEvents()
			.count()).isEqualTo(0);

		reg.unregister();
	}

	@Test
	public void testObservatorServiceReference(@InjectEventListener EventStore eventStore,
		@InjectBundleContext BundleContext bc) throws Exception {

		Filter filter = bc.createFilter(String.format("(objectClass=%s)", A.class.getName()));

		Predicate<ServiceTracker<A, A>> matcher = (tracker) -> {
			return tracker.getServiceReferences() != null && tracker.getServiceReferences().length >= 2;
		};

		Observer<ServiceReferenceData<A>> observer = eventStore.newServiceReferenceObervator(filter, matcher, true);

		List<ServiceRegistration<?>> regs = new ArrayList<ServiceRegistration<?>>();

		for (int i = 0; i < 5; i++) {
			Thread.sleep(10);
			new Thread(() -> {
				regs.add(bc.registerService(A.class, new A() {},
					Dictionaries.dictionaryOf("a", System.currentTimeMillis())));
			}).start();
		}

		Result<ServiceReferenceData<A>> result = observer.waitFor(200, TimeUnit.MILLISECONDS);
		assertThat(result).isNotNull();
		assertThat(result.isTimedOut()).isFalse();
		assertThat(result.get()).isNotNull();

		// assertThat(result.get()
		// .getAddedCount()).isEqualTo(2);
		assertThat(result.get()
			.getModifiedCount()).isEqualTo(0);
		assertThat(result.get()
			.getRemovedCount()).isEqualTo(0);
		assertThat(result.get()
			.getServiceReference()).hasSize(2);

		regs.forEach((r) -> r.unregister());
	}

	interface A {

	}
}
