package org.osgi.test.common.listener;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

public class EventsTest {
	// TODO: more Tests
	@Test
	void testName() throws Exception {
		ServiceReference<?> sr = Mockito.mock(ServiceReference.class);
		ServiceEvent event = new ServiceEvent(ServiceEvent.REGISTERED, sr);
		assertThat(Events.serviceEventType(ServiceEvent.REGISTERED)
			.test(event)).isTrue();
	}
}
