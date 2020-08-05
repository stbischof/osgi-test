package org.osgi.test.common.annotation.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WithConfigurationUtilTest {

	private static String	service_pid						= "my.pid";

	private static String	factoryservice_pid_autogen		= "my.factory.pid~";

	private static String	factoryservice_pid_factoryname	= "my.factory.pid~factoryName";

	@Test
	public void pid() {
		assertEquals(service_pid, WithConfigurationUtil.pid(service_pid));
		assertEquals("my.factory.pid", WithConfigurationUtil.pid(factoryservice_pid_autogen));
		assertEquals("my.factory.pid", WithConfigurationUtil.pid(factoryservice_pid_factoryname));

	}

	@Test
	public void factoryName() {
		assertThrows(IllegalArgumentException.class, () -> WithConfigurationUtil.factoryName(service_pid));
		assertEquals(36, WithConfigurationUtil.factoryName(factoryservice_pid_autogen)
			.length());
		assertEquals("factoryName", WithConfigurationUtil.factoryName(factoryservice_pid_factoryname));

	}

	@Test
	public void isFactory() {
		assertFalse(WithConfigurationUtil.isFactory(service_pid));

		assertTrue(WithConfigurationUtil.isFactory(factoryservice_pid_autogen));

		assertTrue(WithConfigurationUtil.isFactory(factoryservice_pid_factoryname));

	}
}
