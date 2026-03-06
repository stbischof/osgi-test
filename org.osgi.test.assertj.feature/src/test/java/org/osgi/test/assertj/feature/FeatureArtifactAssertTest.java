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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.feature.FeatureArtifact;
import org.osgi.service.feature.ID;

public class FeatureArtifactAssertTest {

	private FeatureArtifact artifact;
	private ID				id;

	@BeforeEach
	void setUp() {
		artifact = mock(FeatureArtifact.class, "testArtifact");
		id = mock(ID.class);
		when(id.getGroupId()).thenReturn("org.test");
		when(id.getArtifactId()).thenReturn("test-artifact");
		when(id.getVersion()).thenReturn("3.0.0");
		when(artifact.getID()).thenReturn(id);
	}

	@Test
	void hasIDThat_returns_id_assert() {
		FeatureArtifactAssert.assertThat(artifact)
			.hasIDThat()
			.hasGroupId("org.test")
			.hasArtifactId("test-artifact")
			.hasVersion("3.0.0");
	}

	@Test
	void null_artifact_fails_isNotNull() {
		assertThatThrownBy(() -> FeatureArtifactAssert.assertThat(null)
			.hasIDThat())
			.isInstanceOf(AssertionError.class);
	}
}
