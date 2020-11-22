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
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.event.EventObservator.Result;

@ExtendWith(BundleContextExtension.class)
public class EventStoreImplTest {
	Bundle			bundle;
	EventStoreImpl	store;

	@BeforeEach
	public void beforeEachTest(@InjectBundleContext BundleContext bc) {

		this.bundle = bc.getBundle();
		store = new EventStoreImpl();
		// All clear
		assertThat(store.getEvents()
			.count()).isEqualTo(0);
	}

	@Test
	public void testFrameworkEvent() throws Exception {

		// create osgi Listener and add
		List<FrameworkEvent> eventsListener = new ArrayList<FrameworkEvent>();
		FrameworkListener listener = new FrameworkListener() {

			@Override
			public void frameworkEvent(FrameworkEvent event) {
				eventsListener.add(event);
			}
		};
		store.addFrameworkListenerDelegate(listener);

		// Create Observer that catches Events
		EventObservator<List<FrameworkEvent>> observator = store
			.newFrameworkEventObervator(EventStore.isFrameworkEventType(FrameworkEvent.ERROR), 1);

		// Create and fire event
		FrameworkEvent event = new FrameworkEvent(FrameworkEvent.ERROR, bundle, null);
		store.frameworkEvent(event);

		// WaitFor getting the event in time
		Result<List<FrameworkEvent>> result = observator.waitFor();
		assertThat(result.isTimedOut()).isFalse();

		// assert all kinds to get the events and compare
		assertThat(store.getEvents()).hasSize(1)
			.hasSameElementsAs(store.getEvents(FrameworkEvent.class)
				.collect(Collectors.toList()))
			.hasSameElementsAs(store.getEventsFramework()
				.collect(Collectors.toList()))
			.hasSameElementsAs(store.getEventsFramework(FrameworkEvent.ERROR)
				.collect(Collectors.toList()))
			.hasSameElementsAs(eventsListener)
			.hasSameElementsAs(result.get())
			.element(0)
			.isEqualTo(event);

		// remove listener
		store.removeFrameworkListenerDelegate(listener);
	}

	@Test
	public void testBundleEvent() throws Exception {

		// create osgi Listener and add
		List<BundleEvent> eventsListener = new ArrayList<BundleEvent>();
		BundleListener listener = new BundleListener() {

			@Override
			public void bundleChanged(BundleEvent event) {
				eventsListener.add(event);
			}
		};
		store.addBundleListenerDelegate(listener);

		// Create Observer that catches Events
		EventObservator<List<BundleEvent>> observator = store
			.newBundleEventObervator(EventStore.isBundleEventType(BundleEvent.INSTALLED), 1);

		// Create and fire event
		BundleEvent event = new BundleEvent(BundleEvent.INSTALLED, bundle, bundle);
		store.bundleChanged(event);

		// WaitFor getting the event in time
		Result<List<BundleEvent>> result = observator.waitFor();
		assertThat(result.isTimedOut()).isFalse();

		// assert all kinds to get the events and compare
		assertThat(store.getEvents()).hasSize(1)
			.hasSameElementsAs(store.getEvents(BundleEvent.class)
				.collect(Collectors.toList()))
			.hasSameElementsAs(store.getEventsBundle()
				.collect(Collectors.toList()))
			.hasSameElementsAs(store.getEventsBundle(BundleEvent.INSTALLED)
				.collect(Collectors.toList()))
			.hasSameElementsAs(eventsListener)
			.hasSameElementsAs(result.get())
			.element(0)
			.isEqualTo(event);

		// remove listener
		store.removeBundleListenerDelegate(listener);

	}

	@Test
	public void testServiceEvent() throws Exception {

		ServiceReference<?> sr = mock(ServiceReference.class);

		// create osgi Listener and add
		List<ServiceEvent> eventsListener = new ArrayList<ServiceEvent>();
		ServiceListener listener = new ServiceListener() {

			@Override
			public void serviceChanged(ServiceEvent event) {
				eventsListener.add(event);
			}
		};

		store.addServiceListenerDelegate(listener);

		// Create Observer that catches Events
		EventObservator<List<ServiceEvent>> observator = store
			.newServiceEventObervator(EventStore.isServiceEventType(ServiceEvent.REGISTERED), 1);

		// Create and fire event
		ServiceEvent event = new ServiceEvent(ServiceEvent.REGISTERED, sr);
		store.serviceChanged(event);

		// WaitFor getting the event in time
		Result<List<ServiceEvent>> result = observator.waitFor();
		assertThat(result.isTimedOut()).isFalse();

		// assert all kinds to get the events and compare
		assertThat(store.getEvents()).hasSize(1)
			.hasSameElementsAs(store.getEvents(ServiceEvent.class)
				.collect(Collectors.toList()))
			.hasSameElementsAs(store.getEventsService()
				.collect(Collectors.toList()))
			.hasSameElementsAs(store.getEventsService(ServiceEvent.REGISTERED)
				.collect(Collectors.toList()))
			.hasSameElementsAs(store.getEventsService(sr, ServiceEvent.REGISTERED)
				.collect(Collectors.toList()))

			.hasSameElementsAs(eventsListener)
			.hasSameElementsAs(result.get())
			.element(0)
			.isEqualTo(event);

		// remove listener
		store.removeServiceListenerDelegate(listener);
	}

	interface A {

	}
}
