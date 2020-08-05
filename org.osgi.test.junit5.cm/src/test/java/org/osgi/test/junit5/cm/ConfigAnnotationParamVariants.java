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
package org.osgi.test.junit5.cm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Dictionary;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.service.cm.Configuration;
import org.osgi.test.common.annotation.config.ConfigEntry;
import org.osgi.test.common.annotation.config.InjectConfiguration;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.common.dictionary.Dictionaries;

@ExtendWith(ConfigurationExtension.class)
@WithConfiguration(pid = ConfigAnnotationParamVariants.MY_PID, properties = {
	@ConfigEntry(key = "a", value = "1")
})
public class ConfigAnnotationParamVariants {

	static final String MY_PID = "my.pid";

	@Test
	public void test_Parameter_Configuration(@InjectConfiguration(MY_PID) Configuration c) throws Exception {
		assertNotNull(c);
		assertDictionary(c.getProperties());
	}

	private void assertDictionary(Dictionary<String, Object> properties) {
		assertEquals("1", properties.get("a"));

	}

	@Test
	public void test_Parameter_Dictionary(
		@InjectConfiguration(MY_PID) Dictionary<String, Object> dictionary)
		throws Exception {
		assertNotNull(dictionary);
		assertDictionary(dictionary);
	}

	@Test
	public void test_Parameter_Map(@InjectConfiguration(MY_PID) Map<String, Object> map) throws Exception {
		assertNotNull(map);
		assertDictionary(Dictionaries.asDictionary(map));
	}

	@Test
	public void test_Parameter_Optional_NotNull(
		@InjectConfiguration(MY_PID) Optional<Configuration> cOptional)
		throws Exception {
		assertNotNull(cOptional);
		assertTrue(cOptional.isPresent());

		assertDictionary(cOptional.get()
			.getProperties());
	}

	@Test
	public void test_Parameter_Optional_Null(
		@InjectConfiguration("unknown.pid") Optional<Configuration> cOptional)
		throws Exception {
		assertNotNull(cOptional);
		assertFalse(cOptional.isPresent());

	}

}
