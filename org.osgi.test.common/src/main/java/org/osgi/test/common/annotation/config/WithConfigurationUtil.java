package org.osgi.test.common.annotation.config;

import java.util.UUID;

public class WithConfigurationUtil {

	private WithConfigurationUtil() {

	}

	public static String pid(WithConfiguration withConfiguration) {
		return pid(withConfiguration.pid());
	}

	public static String pid(InjectConfiguration injectConfiguration) {
		return pid(injectConfiguration.value());
	}

	public static String pid(String pid) {
		if (isFactory(pid)) {
			return pid.substring(0, pid.indexOf("~"));
		} else {
			return pid;
		}
	}

	public static String factoryName(WithConfiguration withConfiguration) {
		return factoryName(withConfiguration.pid());
	}

	public static String factoryName(InjectConfiguration injectConfiguration) {
		return factoryName(injectConfiguration.value());
	}

	public static String factoryName(String pid) {
		if (!isFactory(pid)) {
			throw new IllegalArgumentException("It is not a factory. ~ is missing");
		}
		String tmp = pid.substring(pid.indexOf("~") + 1);

		return tmp.isEmpty() ? UUID.randomUUID()
			.toString() : tmp;
	}

	public static boolean isFactory(WithConfiguration withConfiguration) {
		return isFactory(withConfiguration.pid());
	}

	public static boolean isFactory(InjectConfiguration injectConfiguration) {
		return isFactory(injectConfiguration.value());
	}

	public static boolean isFactory(String pid) {
		return pid.contains("~");
	}
}
