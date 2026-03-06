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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.feature.FeatureBundle;
import org.osgi.service.feature.ID;

public class FeatureBundleAssertTest {

	private FeatureBundle bundle;
	private ID			  id;

	@BeforeEach
	void setUp() {
		bundle = mock(FeatureBundle.class, "testBundle");
		id = mock(ID.class);
		when(id.getGroupId()).thenReturn("org.test");
		when(id.getArtifactId()).thenReturn("test-bundle");
		when(id.getVersion()).thenReturn("2.0.0");
		when(bundle.getID()).thenReturn(id);

		Map<String, Object> metadata = new HashMap<>();
		metadata.put("start-order", Integer.valueOf(10));
		when(bundle.getMetadata()).thenReturn(metadata);
	}

	@Test
	void hasIDThat_returns_id_assert() {
		FeatureBundleAssert.assertThat(bundle)
			.hasIDThat()
			.hasGroupId("org.test")
			.hasArtifactId("test-bundle")
			.hasVersion("2.0.0");
	}

	@Test
	void hasMetadataThat_returns_map_assert() {
		FeatureBundleAssert.assertThat(bundle)
			.hasMetadataThat()
			.containsKey("start-order")
			.containsEntry("start-order", Integer.valueOf(10));
	}

	@Test
	void hasMetadataThat_empty_metadata() {
		when(bundle.getMetadata()).thenReturn(new HashMap<String, Object>());
		FeatureBundleAssert.assertThat(bundle)
			.hasMetadataThat()
			.isEmpty();
	}

	@Test
	void null_bundle_fails_isNotNull() {
		assertThatThrownBy(() -> FeatureBundleAssert.assertThat(null)
			.hasIDThat())
			.isInstanceOf(AssertionError.class);
	}
}
