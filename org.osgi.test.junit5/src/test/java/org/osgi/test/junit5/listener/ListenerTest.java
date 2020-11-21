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

package org.osgi.test.junit5.listener;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.test.common.annotation.InjectListener;
import org.osgi.test.junit5.context.BundleContextExtension;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(ListenerExtension.class)
public class ListenerTest {

	@InjectListener
	static CustomListener	staticlistener;
	@InjectListener
	CustomListener			listener;

	@BeforeAll
	public static void beforeAll() throws Exception {
		assertThat(staticlistener).isNotNull();
	}

	@BeforeEach
	public void beforeEach() throws Exception {
		assertThat(listener).isNotNull();
	}

	@Test
	public void testWithLogServiceField() throws Exception {
		assertThat(listener).isNotNull();
	}

	@Test
	public void testWithlistenerParameter(@InjectListener CustomListener listener) throws Exception {
		assertThat(listener).isNotNull();
	}

	@Test
	public void testWithlistenerParameterProvicerMethod(
		@InjectListener(providerMethod = "createlistener") CustomListener listener) throws Exception {
		assertThat(listener).isNotNull()
			.extracting(l -> l.value)
			.isEqualTo(1);
	}

	public CustomListener createlistener() {
		return new CustomListener(1);
	}

	static class CustomListener implements ServiceListener {
		public int value;

		public CustomListener() {
			value = 0;
		}

		public CustomListener(int value) {
			this.value = value;
		}

		@Override
		public void serviceChanged(ServiceEvent event) {}

	}
}
