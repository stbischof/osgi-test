package org.osgi.test.common.listener;

import static org.osgi.test.common.listener.Events.isBundleEventType;
import static org.osgi.test.common.listener.Events.isFrameworkEventType;

import java.util.Collections;
import java.util.Dictionary;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.dictionary.Dictionaries;

public class EventsTest {

	@Test
	void testPredicateBundle() throws Exception {

		SoftAssertions softly = new SoftAssertions();

		//
		Bundle bundle = Mockito.mock(Bundle.class);
		BundleEvent event = new BundleEvent(BundleEvent.INSTALLED, bundle);

		//
		softly.assertThat(isBundleEventType(BundleEvent.INSTALLED).test(event))
			.isTrue();

		softly.assertAll();
	}

	@Test
	void testPredicateFramework() throws Exception {

		SoftAssertions softly = new SoftAssertions();

		//
		Bundle bundle = Mockito.mock(Bundle.class);
		FrameworkEvent event = new FrameworkEvent(FrameworkEvent.STARTLEVEL_CHANGED, bundle, null);

		//
		softly.assertThat(isFrameworkEventType(FrameworkEvent.STARTLEVEL_CHANGED).test(event))
			.isTrue();

		softly.assertAll();
	}

	@Test
	void testPredicateService() throws Exception {

		SoftAssertions softly = new SoftAssertions();

		//

		Dictionary<String, Object> dict = Dictionaries.dictionaryOf(Constants.OBJECTCLASS, A.class.getName(), "key1", 1,
			"key2", 2);

		ServiceReference<?> sr = Mockito.mock(ServiceReference.class);
		Mockito.when(sr.getPropertyKeys())
			.thenReturn(Collections.list(dict.keys())
				.toArray(new String[3]));

		Mockito.when(sr.getProperty(Mockito.any(String.class)))
			.then(new Answer<Object>() {

				@Override
				public Object answer(InvocationOnMock invocation) throws Throwable {
					String key = invocation.getArgument(0);
					return dict.get(key);
				}
			});

		ServiceEvent event = new ServiceEvent(ServiceEvent.REGISTERED, sr);

		//
		softly.assertThat(Events.isServiceEventType(ServiceEvent.REGISTERED)
			.test(event))
			.isTrue();

		softly.assertThat(Events.containsServiceProperties(dict)
			.test(event))
			.isTrue();

		softly.assertThat(Events.containsServiceProperties(Dictionaries.asMap(dict))
			.test(event))
			.isTrue();

		softly.assertThat(Events.containsServiceProperty(Constants.OBJECTCLASS, A.class.getName())
			.test(event))
			.isTrue();

		softly.assertThat(Events.containsServiceProperty("key1", 1)
			.test(event))
			.isTrue();

		softly.assertThat(Events.containsServiceProperty("key1", 2)
			.test(event))
			.isFalse();

		softly.assertThat(Events.containsServiceProperty("key3", 3)
			.test(event))
			.isFalse();


		softly.assertThat(Events.isServiceRegistered(A.class)
			.test(event))
			.isTrue();

		softly.assertThat(Events.hasServiceObjectClass(A.class)
			.test(event))
			.isTrue();

		softly.assertThat(Events.isServiceModified(A.class)
			.test(event))
			.isFalse();

		softly.assertThat(Events.isServiceEventWith(ServiceEvent.REGISTERED, A.class, dict)
			.test(event))
			.isTrue();

		softly.assertThat(Events.isServiceEventWith(ServiceEvent.REGISTERED, A.class, Dictionaries.dictionaryOf())
			.test(event))
			.isTrue();

		softly.assertAll();
	}

	interface A {}
}
