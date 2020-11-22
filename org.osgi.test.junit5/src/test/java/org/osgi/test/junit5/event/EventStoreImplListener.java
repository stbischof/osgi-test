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

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.test.common.annotation.InjectEventListener;
import org.osgi.test.common.annotation.InjectInstallBundle;
import org.osgi.test.common.install.InstallBundle;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.event.EventObservator.Result;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(EventListenerExtension.class)
public class EventStoreImplListener {

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
		EventObservator<List<BundleEvent>> eventObservator = eventStore.newBundleEventObervator(matchesAnyInstalled, 1);

		// installe a Bundle to get an INSTALL-Event
		Bundle b = ib.installBundle("tb1.jar", false);

		// Wait for the event
		Result<List<BundleEvent>> result = eventObservator.waitFor(2000, TimeUnit.MILLISECONDS);

		// Check that the event happened
		assertThat(result).isNotNull();
		assertThat(result.isValid());

		// Check Eventcount from Result and Stream
		List<BundleEvent> observatorResultEvents = result.get();
		assertThat(eventStore.getEvents()).hasSize(1)
			.containsAll(observatorResultEvents);

		// reset
		eventStore.resetEvents();

		// is reseted?
		assertThat(eventStore.getEvents()
			.count()).isEqualTo(0);

	}
}
