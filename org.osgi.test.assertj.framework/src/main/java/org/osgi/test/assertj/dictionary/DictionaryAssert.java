/*
 * Copyright (c) OSGi Alliance (2020). All Rights Reserved.
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

package org.osgi.test.assertj.dictionary;

import java.util.Dictionary;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.MapAssert;
import org.osgi.test.common.dictionary.Dictionaries;

public class DictionaryAssert {
	@SuppressWarnings({
		"rawtypes"
	}) // rawtypes: using Class instance
	public static final <K, V> InstanceOfAssertFactory<Dictionary, MapAssert<K, V>> dictionary(Class<K> keyType,
		Class<V> valueType) {
		return new InstanceOfAssertFactory<>(Dictionary.class, DictionaryAssert::<K, V> assertThat);
	}

	public static <K, V> MapAssert<K, V> assertThat(Dictionary<K, V> actual) {
		return Assertions.assertThat(Dictionaries.asMap(actual));
	}
}
