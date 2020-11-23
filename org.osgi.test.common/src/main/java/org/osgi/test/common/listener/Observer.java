package org.osgi.test.common.listener;

import java.util.concurrent.TimeUnit;

public interface Observer<T> {

	/**
	 * Waits for the expected Event or the timeout. Must call start if not
	 * started;
	 *
	 * @return true if the the expected Result the Observer is waiting for
	 *         matched.
	 */
	boolean waitFor(long timeout, TimeUnit timeUnit);

	/**
	 * Waits for the expected Event or the default timeout. Must call start if
	 * not started;
	 *
	 * @return true if the the expected Result the Observer is waiting for
	 *         matched.
	 */
	boolean waitFor();

	/**
	 * starts to observe
	 */
	void start();
	/*
	 * Result of an Observer. Ihe Data will be the incomplete Object found until
	 * timeout.
	 */

	Result<T> getResult();

	/**
	 * Result of an Observer.
	 *
	 * @param <E> Type of the stored data
	 */
	interface Result<E> {

		/**
		 * Indicated that the waitFor process of the Observer times out.
		 */
		boolean isTimedOut();

		/**
		 * Gives access to the Objects the Observer found. If isTimedOut=true
		 * the Data will be the incomplete Object found until timeout.
		 *
		 * @return Objects the Observer found.
		 */
		E get();
	}

}
