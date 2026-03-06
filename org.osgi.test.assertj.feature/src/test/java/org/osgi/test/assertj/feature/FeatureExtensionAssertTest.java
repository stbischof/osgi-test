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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.feature.FeatureArtifact;
import org.osgi.service.feature.FeatureExtension;
import org.osgi.service.feature.ID;

public class FeatureExtensionAssertTest {

	private FeatureExtension extension;

	@BeforeEach
	void setUp() {
		extension = mock(FeatureExtension.class, "testExtension");
		when(extension.getName()).thenReturn("test-extension");
		when(extension.getType()).thenReturn(FeatureExtension.Type.JSON);
		when(extension.getKind()).thenReturn(FeatureExtension.Kind.MANDATORY);
		when(extension.getJSON()).thenReturn("{\"key\":\"value\"}");
		when(extension.getArtifacts()).thenReturn(new ArrayList<FeatureArtifact>());
	}

	@Test
	void hasName_passes_when_equal() {
		FeatureExtensionAssert.assertThat(extension)
			.hasName("test-extension");
	}

	@Test
	void hasName_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureExtensionAssert.assertThat(extension)
			.hasName("other"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("test-extension");
	}

	@Test
	void hasType_passes_when_equal() {
		FeatureExtensionAssert.assertThat(extension)
			.hasType(FeatureExtension.Type.JSON);
	}

	@Test
	void hasType_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureExtensionAssert.assertThat(extension)
			.hasType(FeatureExtension.Type.TEXT))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasTypeJson() {
		FeatureExtensionAssert.assertThat(extension)
			.hasTypeJson();
	}

	@Test
	void hasTypeText() {
		when(extension.getType()).thenReturn(FeatureExtension.Type.TEXT);
		FeatureExtensionAssert.assertThat(extension)
			.hasTypeText();
	}

	@Test
	void hasTypeArtifacts() {
		when(extension.getType()).thenReturn(FeatureExtension.Type.ARTIFACTS);
		FeatureExtensionAssert.assertThat(extension)
			.hasTypeArtifacts();
	}

	@Test
	void hasKind_passes_when_equal() {
		FeatureExtensionAssert.assertThat(extension)
			.hasKind(FeatureExtension.Kind.MANDATORY);
	}

	@Test
	void hasKind_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureExtensionAssert.assertThat(extension)
			.hasKind(FeatureExtension.Kind.OPTIONAL))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasKindMandantory() {
		FeatureExtensionAssert.assertThat(extension)
			.hasKindMandantory();
	}

	@Test
	void hasKindOptional() {
		when(extension.getKind()).thenReturn(FeatureExtension.Kind.OPTIONAL);
		FeatureExtensionAssert.assertThat(extension)
			.hasKindOptional();
	}

	@Test
	void hasKindTransient() {
		when(extension.getKind()).thenReturn(FeatureExtension.Kind.TRANSIENT);
		FeatureExtensionAssert.assertThat(extension)
			.hasKindTransient();
	}

	@Test
	void hasJSON_passes_when_equal() {
		FeatureExtensionAssert.assertThat(extension)
			.hasJSON("{\"key\":\"value\"}");
	}

	@Test
	void hasJSON_fails_when_not_equal() {
		assertThatThrownBy(() -> FeatureExtensionAssert.assertThat(extension)
			.hasJSON("{\"other\":true}"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasJSONMatching_passes_when_matches() {
		FeatureExtensionAssert.assertThat(extension)
			.hasJSONMatching(".*key.*value.*");
	}

	@Test
	void hasJSONMatching_fails_when_not_matching() {
		assertThatThrownBy(() -> FeatureExtensionAssert.assertThat(extension)
			.hasJSONMatching("nomatch.*"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasJSONMatching_fails_when_null() {
		when(extension.getJSON()).thenReturn(null);
		assertThatThrownBy(() -> FeatureExtensionAssert.assertThat(extension)
			.hasJSONMatching(".*"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasArtefactsThat_returns_list_assert() {
		FeatureExtensionAssert.assertThat(extension)
			.hasArtefactsThat()
			.isEmpty();
	}

	@Test
	void chaining_works() {
		FeatureExtensionAssert.assertThat(extension)
			.hasName("test-extension")
			.hasTypeJson()
			.hasKindMandantory()
			.hasJSON("{\"key\":\"value\"}");
	}

	@Test
	void null_extension_fails_isNotNull() {
		assertThatThrownBy(() -> FeatureExtensionAssert.assertThat(null)
			.hasName("any"))
			.isInstanceOf(AssertionError.class);
	}
}
