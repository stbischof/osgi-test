package org.osgi.test.junit5.event;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.osgi.test.common.exceptions.Exceptions;

public abstract class AbstractObservator<T> implements EventObservator<T> {

	@Override
	public Result<T> waitFor() {
		return waitFor(200, TimeUnit.MILLISECONDS);
	}

	@Override
	public Result<T> waitFor(long timeout, TimeUnit timeUnit) {
		try {
			boolean ok = countDownLatch().await(timeout, timeUnit);
			return new DefaultResult<T>(!ok, get());
		} catch (InterruptedException e) {
			Exceptions.unchecked(() -> e);
		} finally {
			cleanUp();
		}
		throw new AssertionError("unreachable");
	}

	protected abstract T get();

	protected abstract void cleanUp();

	abstract CountDownLatch countDownLatch();

	class DefaultResult<E> implements Result<E> {
		private boolean	timedOut;
		private E		events;

		public DefaultResult(boolean timedOut, E objects) {
			this.timedOut = timedOut;
			this.events = objects;
		}

		@Override
		public boolean isTimedOut() {
			return timedOut;
		}

		@Override
		public E get() {
			return events;
		}
	}
}
