package org.osgi.test.common.listener.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public abstract class AbstractServiceEventObserver<E> extends AbstracEventObserver<E> {
	private ServiceListener		listener;

	private List<ServiceEvent>	objects	= new ArrayList<>();

	public AbstractServiceEventObserver(BundleContext bundleContext, Predicate<ServiceEvent> matches, int count,
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

}
