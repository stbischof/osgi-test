package org.osgi.test.common.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public abstract class ServiceEventObserver<E> extends AbstracEventObserver<E> {
	private ServiceListener		listener;

	private List<ServiceEvent>	objects	= new ArrayList<>();

	public ServiceEventObserver(BundleContext bundleContext, Predicate<ServiceEvent> matches, int count,
		boolean immidiate) {
		super(count, immidiate, bundleContext);

		listener = new ServiceListener() {

			@Override
			public void serviceChanged(ServiceEvent event) {
				if (matches.test(event)) {
					objects().add(event);
					countDownLatch().countDown();
				}
			}
		};
		if (immidiate) {
			start();
		}
	}

	@Override
	protected void unregister() {
		bundleContext().removeServiceListener(listener);
	}

	@Override
	protected void register() {
		bundleContext().addServiceListener(listener);
	}

	public List<ServiceEvent> objects() {
		return objects;
	}

	public static class Single extends ServiceEventObserver<Optional<ServiceEvent>> {

		public Single(BundleContext bundleContext, Predicate<ServiceEvent> matches, boolean immidiate) {
			super(bundleContext, matches, 1, immidiate);
		}

		@Override
		protected Optional<ServiceEvent> getResultObject() {
			return objects().isEmpty() ? Optional.empty() : Optional.of(objects().get(0));
		}

	}

	public static class Multiple extends ServiceEventObserver<List<ServiceEvent>> {

		public Multiple(BundleContext bundleContext, Predicate<ServiceEvent> matches, int count, boolean immidiate) {
			super(bundleContext, matches, count, immidiate);
		}

		@Override
		protected List<ServiceEvent> getResultObject() {
			return objects();
		}

	}

}
