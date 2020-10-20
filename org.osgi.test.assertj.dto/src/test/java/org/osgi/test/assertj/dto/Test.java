package org.osgi.test.assertj.dto;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.dto.FrameworkDTO;

public class Test {

	@org.junit.jupiter.api.Test
	void testName() throws Exception {

		FrameworkDTO frameworkDTO = FrameworkUtil.getBundle(Bundle.class)
			.adapt(FrameworkDTO.class);
		Assertions.assertThat(frameworkDTO);
	}
}
