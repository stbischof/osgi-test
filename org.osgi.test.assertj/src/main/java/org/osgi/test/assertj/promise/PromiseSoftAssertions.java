/*
 * Copyright (c) OSGi Alliance (2019). All Rights Reserved.
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

package org.osgi.test.assertj.promise;

import org.assertj.core.api.SoftAssertions;
import org.osgi.util.promise.Promise;

public class PromiseSoftAssertions extends SoftAssertions {
	/**
	 * Create assertion for {@link org.osgi.util.promise.Promise}.
	 *
	 * @param actual the actual value.
	 * @param <RESULT> the type of the value contained in the
	 *            {@link org.osgi.util.promise.Promise}.
	 * @return the created assertion object.
	 */
	public <RESULT> PromiseAssert<RESULT> assertThat(Promise<RESULT> actual) {
		@SuppressWarnings("unchecked")
		PromiseAssert<RESULT> softly = proxy(PromiseAssert.class, Promise.class, actual);
		return softly;
	}
}
