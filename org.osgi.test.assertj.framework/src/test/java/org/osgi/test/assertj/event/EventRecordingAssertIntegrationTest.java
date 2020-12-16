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

package org.osgi.test.assertj.event;

import static org.osgi.framework.ServiceEvent.REGISTERED;
import static org.osgi.test.common.dictionary.Dictionaries.dictionaryOf;

import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceRegistration;

public class EventRecordingAssertIntegrationTest {

	BundleContext	bc	= FrameworkUtil.getBundle(EventRecordingAssertIntegrationTest.class)
		.getBundleContext();
	String			k1	= "key1";
	String			v1	= "value1";

	String			k2	= "key2";
	String			v2	= "value2";

	@Test
	void exampleIntegrationTest() throws Exception {

		// Setup assert
		EventRecordingAssert eAssert = EventRecordingAssert.assertThatServiceEvent(() -> {
			ServiceRegistration<A> reg = null;
			reg = bc.registerService(A.class, new A() {}, dictionaryOf(k1, v1));
			reg.setProperties(dictionaryOf(k1, v1, k2, v2));
			reg.unregister();
		}, (e) -> e.getType() == ServiceEvent.UNREGISTERING, 20);

		// check whether the Predicate matches or the timeout
		eAssert.isNotTimedOut();

		// get ListAsserts and check them
		eAssert.hasEventsThat()
			.isNotEmpty();

		eAssert.hasFrameworkEventsThat()
			.isEmpty();

		eAssert.hasBundleEventsThat()
			.isEmpty();

		// ListAsserts in combination with Conditions
		eAssert.hasServiceEventsThat()
			.first()
			.isOfType(REGISTERED);

		eAssert.hasServiceEventsThat()
			.isNotEmpty()
			.hasSize(3);

		eAssert.hasServiceEventsThat()
			.element(1)
			.isOfType(ServiceEvent.MODIFIED);

		eAssert.hasServiceEventsThat()
			.element(2)
			.isOfType(ServiceEvent.UNREGISTERING);
	}

	class A {}
}
