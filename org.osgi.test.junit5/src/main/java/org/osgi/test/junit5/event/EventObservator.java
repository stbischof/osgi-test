package org.osgi.test.junit5.event;

import java.util.concurrent.TimeUnit;

public interface EventObservator<T> {

	Result<T> waitFor(long timeout, TimeUnit timeUnit);

	Result<T> waitFor();

	interface Result<E> {
		boolean isValid();

		E get();
	}

}
