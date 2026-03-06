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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.feature.ID;

public class IDAssertTest {

	private ID id;

	@BeforeEach
	void setUp() {
		id = mock(ID.class, "testID");
		when(id.getGroupId()).thenReturn("org.test");
		when(id.getArtifactId()).thenReturn("test-artifact");
		when(id.getVersion()).thenReturn("1.2.3");
		when(id.getType()).thenReturn(Optional.of("jar"));
		when(id.getClassifier()).thenReturn(Optional.of("sources"));
	}

	@Test
	void hasGroupId_passes_when_equal() {
		IDAssert.assertThat(id)
			.hasGroupId("org.test");
	}

	@Test
	void hasGroupId_fails_when_not_equal() {
		assertThatThrownBy(() -> IDAssert.assertThat(id)
			.hasGroupId("other.group"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("org.test");
	}

	@Test
	void hasArtifactId_passes_when_equal() {
		IDAssert.assertThat(id)
			.hasArtifactId("test-artifact");
	}

	@Test
	void hasArtifactId_fails_when_not_equal() {
		assertThatThrownBy(() -> IDAssert.assertThat(id)
			.hasArtifactId("other"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("test-artifact");
	}

	@Test
	void hasVersion_passes_when_equal() {
		IDAssert.assertThat(id)
			.hasVersion("1.2.3");
	}

	@Test
	void hasVersion_fails_when_not_equal() {
		assertThatThrownBy(() -> IDAssert.assertThat(id)
			.hasVersion("2.0.0"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("1.2.3");
	}

	@Test
	void hasType_passes_when_equal() {
		IDAssert.assertThat(id)
			.hasType("jar");
	}

	@Test
	void hasType_fails_when_not_equal() {
		assertThatThrownBy(() -> IDAssert.assertThat(id)
			.hasType("war"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasType_passes_with_null_when_empty() {
		when(id.getType()).thenReturn(Optional.empty());
		IDAssert.assertThat(id)
			.hasType(null);
	}

	@Test
	void hasClassifier_passes_when_equal() {
		IDAssert.assertThat(id)
			.hasClassifier("sources");
	}

	@Test
	void hasClassifier_fails_when_not_equal() {
		assertThatThrownBy(() -> IDAssert.assertThat(id)
			.hasClassifier("javadoc"))
			.isInstanceOf(AssertionError.class);
	}

	@Test
	void hasClassifier_passes_with_null_when_empty() {
		when(id.getClassifier()).thenReturn(Optional.empty());
		IDAssert.assertThat(id)
			.hasClassifier(null);
	}

	@Test
	void chaining_works() {
		IDAssert.assertThat(id)
			.hasGroupId("org.test")
			.hasArtifactId("test-artifact")
			.hasVersion("1.2.3")
			.hasType("jar")
			.hasClassifier("sources");
	}

	@Test
	void null_id_fails_isNotNull() {
		assertThatThrownBy(() -> IDAssert.assertThat(null)
			.hasGroupId("any"))
			.isInstanceOf(AssertionError.class);
	}
}
