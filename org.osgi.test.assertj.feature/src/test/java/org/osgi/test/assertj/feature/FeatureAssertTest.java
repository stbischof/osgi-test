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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.feature.Feature;
import org.osgi.service.feature.FeatureBundle;
import org.osgi.service.feature.FeatureConfiguration;
import org.osgi.service.feature.FeatureExtension;
import org.osgi.service.feature.ID;

public class FeatureAssertTest {

	private Feature feature;

	@BeforeEach
	void setUp() {
		feature = mock(Feature.class, "testFeature");
		when(feature.getName()).thenReturn(Optional.of("myFeature"));
		when(feature.getDescription()).thenReturn(Optional.of("A test feature"));
		when(feature.getVendor()).thenReturn(Optional.of("TestVendor"));
		when(feature.getLicense()).thenReturn(Optional.of("Apache-2.0"));
		when(feature.isComplete()).thenReturn(true);

		ID id = mock(ID.class);
		when(id.getGroupId()).thenReturn("org.test");
		when(id.getArtifactId()).thenReturn("test-feature");
		when(id.getVersion()).thenReturn("1.0.0");
		when(feature.getID()).thenReturn(id);

		List<FeatureBundle> bundles = new ArrayList<>();
		when(feature.getBundles()).thenReturn(bundles);

		Map<String, FeatureConfiguration> configs = new HashMap<>();
		when(feature.getConfigurations()).thenReturn(configs);

		Map<String, FeatureExtension> extensions = new HashMap<>();
		when(feature.getExtensions()).thenReturn(extensions);

		Map<String, Object> variables = new HashMap<>();
		when(feature.getVariables()).thenReturn(Collections.<String, Object> emptyMap());
	}

	@Test
	void hasName_passes_when_equal() {
		FeatureAssert.assertThat(feature)
			.hasName("myFeature");
	}

	@Test
	void hasName_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasName("otherName"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("myFeature");
	}

	@Test
	void hasName_passes_with_empty_optional() {
		when(feature.getName()).thenReturn(Optional.empty());
		FeatureAssert.assertThat(feature)
			.hasName(null);
	}

	@Test
	void hasNameMatching_passes_when_matches() {
		FeatureAssert.assertThat(feature)
			.hasNameMatching("my.*");
	}

	@Test
	void hasNameMatching_fails_when_not_matching() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasNameMatching("other.*"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasNameMatching_fails_when_null() {
		when(feature.getName()).thenReturn(Optional.empty());
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasNameMatching("my.*"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasNameEmpty_passes_when_empty() {
		when(feature.getName()).thenReturn(Optional.empty());
		FeatureAssert.assertThat(feature)
			.hasNameEmpty();
	}

	@Test
	void hasNameEmpty_fails_when_present() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasNameEmpty())
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("myFeature");
	}

	@Test
	void hasDescription_passes_when_equal() {
		FeatureAssert.assertThat(feature)
			.hasDescription("A test feature");
	}

	@Test
	void hasDescription_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasDescription("wrong"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasDescriptionMatching_passes_when_matches() {
		FeatureAssert.assertThat(feature)
			.hasDescriptionMatching("A test.*");
	}

	@Test
	void hasDescriptionMatching_fails_when_not_matching() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasDescriptionMatching("wrong.*"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void isDescriptionEmpty_passes_when_empty() {
		when(feature.getDescription()).thenReturn(Optional.empty());
		FeatureAssert.assertThat(feature)
			.isDescriptionEmpty();
	}

	@Test
	void isDescriptionEmpty_fails_when_present() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.isDescriptionEmpty())
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasVendor_passes_when_equal() {
		FeatureAssert.assertThat(feature)
			.hasVendor("TestVendor");
	}

	@Test
	void hasVendor_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasVendor("wrong"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasVendorMatching_passes_when_matches() {
		FeatureAssert.assertThat(feature)
			.hasVendorMatching("Test.*");
	}

	@Test
	void hasVendorMatching_fails_when_not_matching() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasVendorMatching("wrong.*"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void isVendorEmpty_passes_when_empty() {
		when(feature.getVendor()).thenReturn(Optional.empty());
		FeatureAssert.assertThat(feature)
			.isVendorEmpty();
	}

	@Test
	void isVendorEmpty_fails_when_present() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.isVendorEmpty())
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasLicense_passes_when_equal() {
		FeatureAssert.assertThat(feature)
			.hasLicense("Apache-2.0");
	}

	@Test
	void hasLicense_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasLicense("MIT"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasLicenseMatching_passes_when_matches() {
		FeatureAssert.assertThat(feature)
			.hasLicenseMatching("Apache.*");
	}

	@Test
	void hasLicenseMatching_fails_when_not_matching() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.hasLicenseMatching("MIT.*"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void isLicenseEmpty_passes_when_empty() {
		when(feature.getLicense()).thenReturn(Optional.empty());
		FeatureAssert.assertThat(feature)
			.isLicenseEmpty();
	}

	@Test
	void isLicenseEmpty_fails_when_present() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.isLicenseEmpty())
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void isComplete_passes_when_complete() {
		FeatureAssert.assertThat(feature)
			.isComplete();
	}

	@Test
	void isComplete_fails_when_not_complete() {
		when(feature.isComplete()).thenReturn(false);
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.isComplete())
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("complete");
	}

	@Test
	void isNotComplete_passes_when_not_complete() {
		when(feature.isComplete()).thenReturn(false);
		FeatureAssert.assertThat(feature)
			.isNotComplete();
	}

	@Test
	void isNotComplete_fails_when_complete() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(feature)
			.isNotComplete())
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("complete");
	}

	@Test
	void hasIDThat_returns_id_assert() {
		FeatureAssert.assertThat(feature)
			.hasIDThat()
			.hasGroupId("org.test")
			.hasArtifactId("test-feature")
			.hasVersion("1.0.0");
	}

	@Test
	void hasBundlesThat_returns_list_assert() {
		FeatureAssert.assertThat(feature)
			.hasBundlesThat()
			.isEmpty();
	}

	@Test
	void hasConfigurationsThat_returns_map_assert() {
		FeatureAssert.assertThat(feature)
			.hasConfigurationsThat()
			.isEmpty();
	}

	@Test
	void hasExtensionsThat_returns_map_assert() {
		FeatureAssert.assertThat(feature)
			.hasExtensionsThat()
			.isEmpty();
	}

	@Test
	void hasVariablesThat_returns_map_assert() {
		FeatureAssert.assertThat(feature)
			.hasVariablesThat()
			.isEmpty();
	}

	@Test
	void chaining_works() {
		FeatureAssert.assertThat(feature)
			.hasName("myFeature")
			.hasDescription("A test feature")
			.hasVendor("TestVendor")
			.hasLicense("Apache-2.0")
			.isComplete();
	}

	@Test
	void null_feature_fails_isNotNull() {
		assertThatThrownBy(() -> FeatureAssert.assertThat(null)
			.hasName("any"))
			.isInstanceOf(AssertionError.class);
	}
}
