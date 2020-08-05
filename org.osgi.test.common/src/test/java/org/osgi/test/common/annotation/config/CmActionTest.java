package org.osgi.test.common.annotation.config;

import org.junit.jupiter.api.Test;

public class CmActionTest {

	public static void enumCodeCoverage(Class<? extends Enum<?>> enumerationClass) {
		try {
			for (Object o : (Object[]) enumerationClass.getMethod("values")
				.invoke(null)) {
				enumerationClass.getMethod("valueOf", String.class)
					.invoke(null, o.toString());
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testEnum() {
		enumCodeCoverage(CmAction.class);
	}
}
