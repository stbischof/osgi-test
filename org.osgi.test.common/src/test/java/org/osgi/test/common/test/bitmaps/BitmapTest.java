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
package org.osgi.test.common.test.bitmaps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.osgi.framework.BundleEvent;
import org.osgi.test.common.bitmaps.Bitmap;
import org.osgi.test.common.bitmaps.BundleEventType;
import org.osgi.test.common.bitmaps.FrameworkEventType;
import org.osgi.test.common.bitmaps.ServiceEventType;

public class BitmapTest {

	static Stream<Arguments> bitmaps() {
		return Stream.of(Arguments.of("BundleEvent", BundleEventType.BITMAP),
			Arguments.of("ServiceEvent", ServiceEventType.BITMAP),
			Arguments.of("FrameworkEvent", FrameworkEventType.BITMAP));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("bitmaps")
	public void unknownMaskIsComplementOfKnownMask(String name, Bitmap bitmap) {
		int known = bitmap.getKnownMask();
		int unknown = bitmap.getUnknownMask();

		assertThat(known).isNotZero();
		assertThat(known & unknown).as("known and unknown masks must not overlap")
			.isZero();
		assertThat(known | unknown).as("known and unknown masks together cover all bits")
			.isEqualTo(-1);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("bitmaps")
	public void everyTypeIsKnown(String name, Bitmap bitmap) {
		int unknown = bitmap.getUnknownMask();
		for (int type = 1; type != 0; type <<= 1) {
			if (Bitmap.typeMatchesMask(type, bitmap.getKnownMask())) {
				assertThat(type & unknown).as("type %d", type)
					.isZero();
			}
		}
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("bitmaps")
	public void knownMaskOfBundleEventsCoversAllBundleEventTypes(String name, Bitmap bitmap) {
		if (bitmap != BundleEventType.BITMAP) {
			return;
		}
		int all = BundleEvent.INSTALLED | BundleEvent.STARTED | BundleEvent.STOPPED | BundleEvent.UPDATED
			| BundleEvent.UNINSTALLED | BundleEvent.RESOLVED | BundleEvent.UNRESOLVED | BundleEvent.STARTING
			| BundleEvent.STOPPING | BundleEvent.LAZY_ACTIVATION;
		assertThat(bitmap.getKnownMask()).isEqualTo(all);
		assertThat(bitmap.getUnknownMask()).isEqualTo(~all);
	}
}
