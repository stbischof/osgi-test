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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.config.ConfigEntry;
import org.osgi.test.common.annotation.config.InjectConfiguration;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;

@ExtendWith(ConfigurationExtension.class)
@WithConfiguration(pid = ConfigAnnotationTest.NONSTATIC_CONFIGURATION_PID)
public class ConfigAnnotationTest {

	public static final String							FACTORY_CONFIGURATION_PID	= "my.factory.configuration.pid";
	public static final String							NONSTATIC_CONFIGURATION_PID	= "nonstatic.configuration.pid";

	private static ServiceReference<ConfigurationAdmin>	sref						= null;
	private static BundleContext						bundleContext				= null;
	private static ConfigurationAdmin					ca							= null;

	@BeforeAll
	public static void beforeAll() {

		bundleContext = FrameworkUtil.getBundle(ConfigAnnotationTest.class)
			.getBundleContext();

		sref = bundleContext.getServiceReference(ConfigurationAdmin.class);
		if (sref == null) {
			throw new IllegalStateException("The configuration admin service cannot be found.");
		}

		ca = bundleContext.getService(sref);
		if (ca == null) {
			throw new IllegalStateException("The configuration admin service cannot be found.");
		}

	}

	@AfterAll
	public static void afterAll() {
		BundleContext bundleContext = FrameworkUtil.getBundle(ConfigAnnotationTest.class)
			.getBundleContext();
		if (bundleContext != null && sref != null) {
			bundleContext.ungetService(sref);
		}

	}

	// START TESTS
	@InjectConfiguration(NONSTATIC_CONFIGURATION_PID)
	Configuration nonStaticConfiguration;

	@Test
	public void testFieldConfiguration() throws Exception {

		Configuration cs = ConfigUtil.getConfigsByServicePid(ca, NONSTATIC_CONFIGURATION_PID);

		assertThat(cs).isEqualTo(nonStaticConfiguration);

	}

	static final String PARAM_PID = "param.pid";

	@Test
	@WithConfiguration(pid = PARAM_PID, properties = {
		@ConfigEntry(key = "bar", value = "foo")
	})
	public void testParameterConfiguration(
		@InjectConfiguration(PARAM_PID) Configuration configuration)
		throws Exception {

		Configuration cs = ConfigUtil.getConfigsByServicePid(ca, PARAM_PID);
		assertThat(cs).isEqualTo(configuration);

		assertNull(configuration.getProperties()
			.get("foo"));

		String foo = (String) configuration.getProperties()
			.get("bar");
		assertEquals("foo", foo);
	}

	@Test
	@WithConfiguration(pid = PARAM_PID, properties = {
		@ConfigEntry(key = "foo", value = "bar")
	})
	public void testParameterConfiguration2(
		@InjectConfiguration(PARAM_PID) Configuration configuration)
		throws Exception {

		Configuration cs = ConfigUtil.getConfigsByServicePid(ca, PARAM_PID);
		assertThat(cs).isEqualTo(configuration);

		assertNull(configuration.getProperties()
			.get("bar"));
		String bar = (String) configuration.getProperties()
			.get("foo");
		assertEquals("bar", bar);
	}

	@Test
	@WithConfiguration(pid = PARAM_PID)
	public void testParameterConfiguration3(
		@InjectConfiguration(PARAM_PID) Configuration configuration)
		throws Exception {

		Configuration cs = ConfigUtil.getConfigsByServicePid(ca, PARAM_PID);

		assertNull(configuration.getProperties()
			.get("foo"));
		assertNull(configuration.getProperties()
			.get("bar"));
	}

	static final String METHOD_PID = "method.pid";

	@Test
	@WithConfiguration(pid = METHOD_PID, properties = {
		@ConfigEntry(key = "foo", value = "bar")
	})
	public void testMethodConfiguration() throws Exception {

		Configuration cs = ConfigUtil.getConfigsByServicePid(ca, METHOD_PID);
		assertThat(cs).isNotNull();
		assertEquals("bar", cs.getProperties()
			.get("foo"));
	}

	@Test
	@WithConfiguration(pid = FACTORY_CONFIGURATION_PID + "~" + "factory.name", properties = {
		@ConfigEntry(key = "foo", value = "bar")
	})
	public void testMethodConfigurationFactory() throws Exception {

		Configuration cs = ConfigUtil.getConfigsByServicePid(ca, FACTORY_CONFIGURATION_PID + "~factory.name");
		assertThat(cs).isNotNull();

		assertEquals("bar", cs.getProperties()
			.get("foo"));
	}

	@Test
	@WithFactoryConfiguration(factoryPid = FACTORY_CONFIGURATION_PID, name = "factory.name2", properties =
	{
		@ConfigEntry(key = "foo", value = "bar")
	})
	public void testMethodConfigurationFactoryCreate() throws Exception {

		Configuration cs = ConfigUtil.getConfigsByServicePid(ca, FACTORY_CONFIGURATION_PID + "~factory.name2");
		assertThat(cs).isNotNull();

		assertEquals("bar", cs.getProperties()
			.get("foo"));
	}

}
