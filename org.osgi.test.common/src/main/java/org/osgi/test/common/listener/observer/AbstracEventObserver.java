package org.osgi.test.common.listener.observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.test.common.exceptions.Exceptions;

public abstract class AbstracEventObserver<T> implements Observer<T> {

	private CountDownLatch	countDownLatch;
	private boolean			started;
	private BundleContext	bundleContext;
	private AbstracEventObserver<T>.DefaultResult<T>	waitResult;

	public AbstracEventObserver(int count, boolean immidiate, BundleContext bundleContext) {

		countDownLatch = new CountDownLatch(count);
		List<ServiceEvent> events = Collections.synchronizedList(new ArrayList<>());
		this.bundleContext = bundleContext;

	}

	protected CountDownLatch countDownLatch() {
		return countDownLatch;
	}

	@Override
	public boolean waitFor() {
		return waitFor(200, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean waitFor(long timeout, TimeUnit timeUnit) {
		start();
		try {
			boolean ok = countDownLatch.await(timeout, timeUnit);
			waitResult = new DefaultResult<T>(!ok, getResultObject());
			return ok;
		} catch (InterruptedException e) {
			Exceptions.unchecked(() -> e);
		} finally {
			unregister();
		}
		throw new AssertionError("unreachable");
	}


	@Override
	public Result<T> getResult() {

		if (waitResult == null) {
			throw new RuntimeException("waitFor has to be executed before");
		}
		return waitResult;
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

	protected BundleContext bundleContext() {
		return bundleContext;
	}

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
