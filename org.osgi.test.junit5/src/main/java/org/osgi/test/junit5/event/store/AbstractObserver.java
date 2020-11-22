package org.osgi.test.junit5.event.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.ServiceEvent;
import org.osgi.test.common.exceptions.Exceptions;

public abstract class AbstractObserver<T> implements Observer<T> {

	private CountDownLatch	countDownLatch;
	private boolean			started;

	public AbstractObserver(int count, boolean immidiate) {

		countDownLatch = new CountDownLatch(count);
		List<ServiceEvent> events = Collections.synchronizedList(new ArrayList<>());

	}

	protected CountDownLatch countDownLatch() {
		return countDownLatch;
	}

	@Override
	public Result<T> waitFor() {
		return waitFor(200, TimeUnit.MILLISECONDS);
	}

	@Override
	public Result<T> waitFor(long timeout, TimeUnit timeUnit) {
		start();
		try {
			boolean ok = countDownLatch.await(timeout, timeUnit);
			return new DefaultResult<T>(!ok, getResultObject());
		} catch (InterruptedException e) {
			Exceptions.unchecked(() -> e);
		} finally {
			unregister();
		}
		throw new AssertionError("unreachable");
	}

	@Override
	public void start() {
		if (!started) {
			register();
		}
		this.started = true;
	}

	protected abstract void register();

	protected abstract T getResultObject();

	protected abstract void unregister();

	class DefaultResult<E> implements Result<E> {
		private boolean	timedOut;
		private E		object;

		public DefaultResult(boolean timedOut, E object) {
			this.timedOut = timedOut;
			this.object = object;
		}

		@Override
		public boolean isTimedOut() {
			return timedOut;
		}

		@Override
		public E get() {
			return object;
		}
	}
}
