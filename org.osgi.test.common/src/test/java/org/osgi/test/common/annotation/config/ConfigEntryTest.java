package org.osgi.test.common.annotation.config;

public class ConfigEntryTest {

	@org.junit.jupiter.api.Test
	@WithConfiguration(properties = @ConfigEntry(key = "", primitive = false), pid = "")

	void testName() throws Exception {

	}
}
