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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.osgi.service.feature.Feature;
import org.osgi.service.feature.FeatureArtifact;
import org.osgi.service.feature.FeatureBundle;
import org.osgi.service.feature.FeatureConfiguration;
import org.osgi.service.feature.FeatureExtension;
import org.osgi.service.feature.ID;

public class AssertionsTest {

	@Test
	void assertThat_feature() {
		Feature feature = mock(Feature.class);
		assertThat(Assertions.assertThat(feature)).isInstanceOf(FeatureAssert.class);
	}

	@Test
	void assertThat_featureArtifact() {
		FeatureArtifact artifact = mock(FeatureArtifact.class);
		assertThat(Assertions.assertThat(artifact)).isInstanceOf(FeatureArtifactAssert.class);
	}

	@Test
	void assertThat_featureBundle() {
		FeatureBundle bundle = mock(FeatureBundle.class);
		assertThat(Assertions.assertThat(bundle)).isInstanceOf(FeatureBundleAssert.class);
	}

	@Test
	void assertThat_featureConfiguration() {
		FeatureConfiguration config = mock(FeatureConfiguration.class);
		assertThat(Assertions.assertThat(config)).isInstanceOf(FeatureConfigurationAssert.class);
	}

	@Test
	void assertThat_featureExtension() {
		FeatureExtension extension = mock(FeatureExtension.class);
		assertThat(Assertions.assertThat(extension)).isInstanceOf(FeatureExtensionAssert.class);
	}

	@Test
	void assertThat_id() {
		ID id = mock(ID.class);
		assertThat(Assertions.assertThat(id)).isInstanceOf(IDAssert.class);
	}
}
