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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.service.cm.Configuration;
import org.osgi.test.common.annotation.config.ConfigEntry;
import org.osgi.test.common.annotation.config.ConfigEntry.Type;
import org.osgi.test.common.annotation.config.InjectConfiguration;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;

@ExtendWith(ConfigurationExtension.class)
public class InjectWith {

	@Test
	public void test1(@InjectConfiguration(withConfig = @WithConfiguration(pid = "pid", properties = {//
		@ConfigEntry(key = "key1", value = "val1"), //
		@ConfigEntry(key = "key2", value = {
			"1", "2"
		}, type = Type.Collection, primitive = true)//
	})) Configuration c) throws Exception {
		assertNotNull(c);
	}

	@Test
	public void test2(
		@InjectConfiguration(withFactoryConfig = @WithFactoryConfiguration(factoryPid = "pid2", name = "name")) Configuration c)
		throws Exception {
		assertNotNull(c);
	}

}
