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

package org.osgi.test.common.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.osgi.test.common.listener.Events.isServiceRegistered;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.listener.EventsTest.A;
import org.osgi.test.common.listener.Observer.Result;

public class EventObserverTest {

	static List<FrameworkListener>	frameworkListeners	= new ArrayList<>();
	static List<ServiceListener>	serviceListeners	= new ArrayList<>();
	static List<BundleListener>		bundleListeners		= new ArrayList<>();
	static BundleContext			bundleContext;

	static Bundle					bundle;

	@BeforeAll
	public static void beforeAll() {
		bundle = mock(Bundle.class);

		when(bundle.getSymbolicName()).thenReturn("test");

		bundleContext = mock(BundleContext.class);

		doAnswer(invocation -> {
			Object listener = invocation.getArgument(0);
			bundleListeners.add((BundleListener) listener);
			return null;
		}).when(bundleContext)
			.addBundleListener(Mockito.any(BundleListener.class));

		doAnswer(invocation -> {
			Object listener = invocation.getArgument(0);
			bundleListeners.remove(listener);
			return null;
		}).when(bundleContext)
			.removeBundleListener(Mockito.any(BundleListener.class));

		doAnswer(invocation -> {
			Object listener = invocation.getArgument(0);
			frameworkListeners.add((FrameworkListener) listener);
			return null;
		}).when(bundleContext)
			.addFrameworkListener(Mockito.any(FrameworkListener.class));

		doAnswer(invocation -> {
			Object listener = invocation.getArgument(0);
			frameworkListeners.remove(listener);
			return null;
		}).when(bundleContext)
			.removeFrameworkListener(Mockito.any(FrameworkListener.class));

		doAnswer(invocation -> {
			Object listener = invocation.getArgument(0);
			serviceListeners.add((ServiceListener) listener);
			return null;
		}).when(bundleContext)
			.addServiceListener(Mockito.any(ServiceListener.class));
		doAnswer(invocation -> {
			Object listener = invocation.getArgument(0);
			serviceListeners.remove(listener);
			return null;
		}).when(bundleContext)
			.removeServiceListener(Mockito.any(ServiceListener.class));

	}

	@Test
	public void testObservatorBundle() throws Exception {

		// Test any Match on SymbolicName and INSTALL-Event
		Predicate<BundleEvent> matches = (event) -> (event.getBundle()
			.getSymbolicName()
			.matches("t.*") && (event.getType() & BundleEvent.INSTALLED) != 0);

		// Create an Obervator with the given count and Predicate-Matcher
		Observer<BundleEvent> observerSingle = Events.newBundleEventObserver(bundleContext, matches, true);
		Observer<List<BundleEvent>> observerMulti = Events.newBundleEventObserver(bundleContext, matches, 2, true);

		assertThat(bundleListeners).hasSize(2);
		bundleListeners.forEach((l) -> l.bundleChanged(new BundleEvent(BundleEvent.INSTALLED, bundle, bundle)));

		// Wait for the event

		assertThat(observerSingle.waitFor(200, TimeUnit.MILLISECONDS)).isTrue();
		assertThat(observerMulti.waitFor(1, TimeUnit.MILLISECONDS)).isFalse();
		assertThat(bundleListeners).hasSize(0);

		Result<BundleEvent> resultSingle = observerSingle.getResult();
		Result<List<BundleEvent>> resultMulti = observerMulti.getResult();

		// Check that the event happened
		assertThat(resultSingle).isNotNull();
		assertThat(resultSingle.isTimedOut()).isFalse();
		assertThat(resultSingle.get()).isNotNull();
		assertThat(resultMulti).isNotNull();
		assertThat(resultMulti.isTimedOut()).isTrue();
		assertThat(resultMulti.get()).isNotNull();
		assertThat(resultMulti.get()).hasSize(1);

		assertThat(bundleListeners).isEmpty();
		assertThat(frameworkListeners).isEmpty();
		assertThat(serviceListeners).isEmpty();

	}

	@Test
	public void testObservatorFramework() throws Exception {

		// Test any Match
		Predicate<FrameworkEvent> matches = (event) -> event.getType() == FrameworkEvent.STOPPED;

		// Create an Obervator with the given count and Predicate-Matcher
		Observer<FrameworkEvent> observerSingle = Events.newFrameworkEventObserver(bundleContext, matches, true);
		Observer<List<FrameworkEvent>> observerMulti = Events.newFrameworkEventObserver(bundleContext, matches, 2,
			true);

		assertThat(frameworkListeners).hasSize(2);
		frameworkListeners.forEach((l) -> l.frameworkEvent(new FrameworkEvent(FrameworkEvent.STOPPED, bundle, null)));

		// Wait for the event

		assertThat(observerSingle.waitFor(200, TimeUnit.MILLISECONDS)).isTrue();
		assertThat(observerMulti.waitFor(1, TimeUnit.MILLISECONDS)).isFalse();
		assertThat(frameworkListeners).hasSize(0);

		Result<FrameworkEvent> resultSingle = observerSingle.getResult();
		Result<List<FrameworkEvent>> resultMulti = observerMulti.getResult();

		// Check that the event happened
		assertThat(resultSingle).isNotNull();
		assertThat(resultSingle.isTimedOut()).isFalse();
		assertThat(resultSingle.get()).isNotNull();
		assertThat(resultMulti).isNotNull();
		assertThat(resultMulti.isTimedOut()).isTrue();
		assertThat(resultMulti.get()).isNotNull();
		assertThat(resultMulti.get()).hasSize(1);

		assertThat(bundleListeners).isEmpty();
		assertThat(frameworkListeners).isEmpty();
		assertThat(serviceListeners).isEmpty();

	}

	@Test
	public void testObservatorService() throws Exception {

		// Test any Match
		Predicate<ServiceEvent> matches = (event) -> event.getType() == ServiceEvent.REGISTERED;

		// Create an Obervator with the given count and Predicate-Matcher
		Observer<ServiceEvent> observerSingle = Events.newServiceEventObserver(bundleContext, matches, true);
		Observer<List<ServiceEvent>> observerMulti = Events.newServiceEventObserver(bundleContext, matches, 2, true);

		assertThat(serviceListeners).hasSize(2);
		serviceListeners
			.forEach((l) -> l.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, mock(ServiceReference.class))));

		// Wait for the event

		assertThat(observerSingle.waitFor(200, TimeUnit.MILLISECONDS)).isTrue();
		assertThat(observerMulti.waitFor(1, TimeUnit.MILLISECONDS)).isFalse();
		assertThat(serviceListeners).hasSize(0);

		Result<ServiceEvent> resultSingle = observerSingle.getResult();
		Result<List<ServiceEvent>> resultMulti = observerMulti.getResult();

		// Check that the event happened
		assertThat(resultSingle).isNotNull();
		assertThat(resultSingle.isTimedOut()).isFalse();
		assertThat(resultSingle.get()).isNotNull();
		assertThat(resultMulti).isNotNull();
		assertThat(resultMulti.isTimedOut()).isTrue();
		assertThat(resultMulti.get()).isNotNull();
		assertThat(resultMulti.get()).hasSize(1);

		assertThat(bundleListeners).isEmpty();
		assertThat(frameworkListeners).isEmpty();
		assertThat(serviceListeners).isEmpty();

	}

	@Test
	public void exampleSingle() throws Exception {

		// Create an Observer with the given count (here one) and
		// Predicate-Matches
		Observer<ServiceEvent> observerSingle = Events.newServiceEventObserver(bundleContext,
			isServiceRegistered(A.class), false);

		// starts observing
		observerSingle.start();

		if (observerSingle.waitFor(200, TimeUnit.MILLISECONDS)) {
			// return true when waitFor catches event before timeOut
		} else {
			// return false when waitFor times out
		}

		// Check that the event happened
		Result<ServiceEvent> resultSingle = observerSingle.getResult();

		resultSingle.isTimedOut();

		// use the event
		ServiceEvent event = resultSingle.get();
		event.getServiceReference();
	}

	@Test
	public void exampleMultiple() throws Exception {

		// Create an Observer with the given count and Predicate-Matches
		Observer<List<ServiceEvent>> observerSingle = Events.newServiceEventObserver(bundleContext,
			isServiceRegistered(A.class), 5, false);

		// starts observing
		observerSingle.start();

		if (observerSingle.waitFor(200, TimeUnit.MILLISECONDS)) {
			// return true when waitFor catches 5 events before timeOut
		} else {
			// return false when waitFor times out
		}

		// Check that the event happened
		Result<List<ServiceEvent>> resultSingle = observerSingle.getResult();

		if (resultSingle.isTimedOut()) {
			// events catched until Timeout
			List<ServiceEvent> event = resultSingle.get();
		}

	}

}
