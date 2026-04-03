/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/

package org.osgi.test.common.quiesce;

import java.time.Duration;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.osgi.framework.AllServiceListener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.SynchronousBundleListener;

/**
 * Waits for the OSGi framework to become quiet, i.e. no bundle or service
 * events are fired for a specified period. Implements both
 * {@link SynchronousBundleListener} and {@link AllServiceListener} so the
 * caller controls the scope by choosing which listener to register.
 */
public class FrameworkQuiesce implements SynchronousBundleListener, AllServiceListener {

	private final ReentrantLock	lock	= new ReentrantLock();
	private final Condition		quiet	= lock.newCondition();
	private int					eventCount;
	private long				lastEventNanos;

	private FrameworkQuiesce() {
		this.lastEventNanos = System.nanoTime();
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		onEvent();
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		onEvent();
	}

	private void onEvent() {
		lock.lock();
		try {
			eventCount++;
			lastEventNanos = System.nanoTime();
			quiet.signalAll();
		} finally {
			lock.unlock();
		}
	}

	private void doWaitForQuiet(Duration quietPeriod, Duration timeout)
		throws InterruptedException, QuiesceTimeoutException {
		long quietNanos = quietPeriod.toNanos();
		long deadlineNanos = System.nanoTime() + timeout.toNanos();
		lock.lock();
		try {
			for (;;) {
				long now = System.nanoTime();
				if (now >= deadlineNanos) {
					throw new QuiesceTimeoutException(quietPeriod, timeout, eventCount);
				}
				long quietUntil = lastEventNanos + quietNanos;
				if (now >= quietUntil) {
					return;
				}
				long waitNanos = Math.min(quietUntil, deadlineNanos) - now;
				long remaining = quiet.awaitNanos(waitNanos);
				if (remaining <= 0 && System.nanoTime() >= deadlineNanos) {
					throw new QuiesceTimeoutException(quietPeriod, timeout, eventCount);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	private int getEventCount() {
		lock.lock();
		try {
			return eventCount;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Registers for both bundle and service events, waits for quiet, then
	 * unregisters.
	 *
	 * @param bc the bundle context to register listeners on
	 * @param quietPeriod required silence duration
	 * @param timeout maximum total wait time
	 * @return the number of events received during the wait
	 * @throws InterruptedException if the waiting thread is interrupted
	 * @throws QuiesceTimeoutException if {@code timeout} expires before the
	 *             quiet period is reached
	 */
	public static int waitForQuiet(BundleContext bc, Duration quietPeriod, Duration timeout)
		throws InterruptedException, QuiesceTimeoutException {
		FrameworkQuiesce listener = new FrameworkQuiesce();
		bc.addBundleListener(listener);
		bc.addServiceListener(listener);
		try {
			listener.doWaitForQuiet(quietPeriod, timeout);
			return listener.getEventCount();
		} finally {
			bc.removeBundleListener(listener);
			bc.removeServiceListener(listener);
		}
	}

	/**
	 * Registers for bundle events only, waits for quiet, then unregisters.
	 *
	 * @param bc the bundle context to register the listener on
	 * @param quietPeriod required silence duration
	 * @param timeout maximum total wait time
	 * @return the number of events received during the wait
	 * @throws InterruptedException if the waiting thread is interrupted
	 * @throws QuiesceTimeoutException if {@code timeout} expires before the
	 *             quiet period is reached
	 */
	public static int waitForBundleQuiet(BundleContext bc, Duration quietPeriod, Duration timeout)
		throws InterruptedException, QuiesceTimeoutException {
		FrameworkQuiesce listener = new FrameworkQuiesce();
		bc.addBundleListener(listener);
		try {
			listener.doWaitForQuiet(quietPeriod, timeout);
			return listener.getEventCount();
		} finally {
			bc.removeBundleListener(listener);
		}
	}

	/**
	 * Registers for service events only, waits for quiet, then unregisters.
	 *
	 * @param bc the bundle context to register the listener on
	 * @param quietPeriod required silence duration
	 * @param timeout maximum total wait time
	 * @return the number of events received during the wait
	 * @throws InterruptedException if the waiting thread is interrupted
	 * @throws QuiesceTimeoutException if {@code timeout} expires before the
	 *             quiet period is reached
	 */
	public static int waitForServiceQuiet(BundleContext bc, Duration quietPeriod, Duration timeout)
		throws InterruptedException, QuiesceTimeoutException {
		FrameworkQuiesce listener = new FrameworkQuiesce();
		bc.addServiceListener(listener);
		try {
			listener.doWaitForQuiet(quietPeriod, timeout);
			return listener.getEventCount();
		} finally {
			bc.removeServiceListener(listener);
		}
	}
}
