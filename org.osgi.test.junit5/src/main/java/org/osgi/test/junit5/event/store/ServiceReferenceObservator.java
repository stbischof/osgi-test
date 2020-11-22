package org.osgi.test.junit5.event.store;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.test.junit5.event.store.ServiceReferenceObservator.ServiceReferenceData;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceReferenceObservator<T> extends AbstractObserver<ServiceReferenceData<T>> {

	ServiceTracker<T, T>			tracker;
	Predicate<ServiceTracker<T, T>>	matches;
	private ServiceReferenceData<T>	serviceReferenceData;
	int								addedCount;
	int								modifiedCount;
	int								removedCount;
	Thread							th;

	public ServiceReferenceObservator(BundleContext bundleContext, Filter filter,
		Predicate<ServiceTracker<T, T>> matches, boolean immidiate) {
		super(1, immidiate);
		this.matches = matches;

		tracker = new ServiceTracker<T, T>(bundleContext, filter, null) {

			@Override
			public T addingService(ServiceReference<T> reference) {
				addedCount++;
				return super.addingService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<T> reference, T service) {
				modifiedCount++;
				super.modifiedService(reference, service);

			}

			@Override
			public void removedService(ServiceReference<T> reference, T service) {
				removedCount++;
				super.removedService(reference, service);
			}
		};

		th = new Thread(() -> {
			while (countDownLatch().getCount() > 0 || Thread.interrupted()) {
				if (matches.test(tracker)) {
					serviceReferenceData = createData();
					countDownLatch().countDown();
				}
				Thread.yield();
			}
		});
		if (immidiate) {
			start();
		}
	}

	@Override
	protected void unregister() {
		if (serviceReferenceData == null) {
			serviceReferenceData = createData();
		}
		tracker.close();
		th.interrupt();
	}

	private ServiceReferenceData<T> createData() {
		return new ServiceReferenceData<T>(addedCount, modifiedCount, removedCount,
			Stream.of(tracker.getServiceReferences())
				.collect(Collectors.toList()));
	}

	@Override
	protected void register() {
		th.start();
		tracker.open();
	}

	@Override
	protected ServiceReferenceData<T> getResultObject() {
		return serviceReferenceData;
	}

	static public class ServiceReferenceData<E> {

		private int							addedCount;
		private int							modifiedCount;
		private int							removedCount;
		private List<ServiceReference<E>>	serviceReference;

		public ServiceReferenceData(int addedCount, int modifiedCount, int removedCount,
			List<ServiceReference<E>> serviceReference) {
			super();
			this.addedCount = addedCount;
			this.modifiedCount = modifiedCount;
			this.removedCount = removedCount;
			this.serviceReference = serviceReference;
		}

		public int getAddedCount() {
			return addedCount;
		}

		public int getModifiedCount() {
			return modifiedCount;
		}

		public int getRemovedCount() {
			return removedCount;
		}

		public List<ServiceReference<E>> getServiceReference() {
			return serviceReference;
		}

	}

}
