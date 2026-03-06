/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/

package org.osgi.test.assertj.feature;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.feature.FeatureConfiguration;

public class FeatureConfigurationAssertTest {

	private FeatureConfiguration config;

	@BeforeEach
	void setUp() {
		config = mock(FeatureConfiguration.class, "testConfig");
		when(config.getPid()).thenReturn("com.test.pid");
		when(config.getFactoryPid()).thenReturn(Optional.of("com.test.factory"));

		Map<String, Object> values = new HashMap<>();
		values.put("key1", "value1");
		values.put("key2", Integer.valueOf(42));
		when(config.getValues()).thenReturn(values);
	}

	@Test
	void hasPid_passes_when_equal() {
		FeatureConfigurationAssert.assertThat(config)
			.hasPid("com.test.pid");
	}

	@Test
	void hasPid_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureConfigurationAssert.assertThat(config)
			.hasPid("other.pid"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("com.test.pid");
	}

	@Test
	void hasFactoryPid_passes_when_equal() {
		FeatureConfigurationAssert.assertThat(config)
			.hasFactoryPid("com.test.factory");
	}

	@Test
	void hasFactoryPid_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureConfigurationAssert.assertThat(config)
			.hasFactoryPid("other.factory"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasFactoryPid_passes_with_null_when_empty() {
		when(config.getFactoryPid()).thenReturn(Optional.empty());
		FeatureConfigurationAssert.assertThat(config)
			.hasFactoryPid(null);
	}

	@Test
	void isFactoryConfiguration_passes_when_factory_pid_present() {
		FeatureConfigurationAssert.assertThat(config)
			.isFactoryConfiguration();
	}

	@Test
	void isFactoryConfiguration_fails_when_factory_pid_empty() {
		when(config.getFactoryPid()).thenReturn(Optional.empty());
		assertThatThrownBy(() -> FeatureConfigurationAssert.assertThat(config)
			.isFactoryConfiguration())
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("factory configuration");
	}

	@Test
	void hasValuesThat_returns_map_assert() {
		FeatureConfigurationAssert.assertThat(config)
			.hasValuesThat()
			.containsKey("key1")
			.containsEntry("key2", Integer.valueOf(42));
	}

	@Test
	void chaining_works() {
		FeatureConfigurationAssert.assertThat(config)
			.hasPid("com.test.pid")
			.hasFactoryPid("com.test.factory")
			.isFactoryConfiguration();
	}

	@Test
	void null_config_fails_isNotNull() {
		assertThatThrownBy(() -> FeatureConfigurationAssert.assertThat(null)
			.hasPid("any"))
			.isInstanceOf(AssertionError.class);
	}
}
