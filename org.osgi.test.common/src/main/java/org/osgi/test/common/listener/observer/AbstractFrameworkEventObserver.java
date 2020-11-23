package org.osgi.test.common.listener.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public abstract class AbstractFrameworkEventObserver<E> extends AbstracEventObserver<E> {
	private FrameworkListener		listener;

	private List<FrameworkEvent>	objects	= new ArrayList<>();

	public AbstractFrameworkEventObserver(BundleContext bundleContext, Predicate<FrameworkEvent> matches, int count,
		boolean immidiate) {
		super(count, immidiate, bundleContext);

		listener = new FrameworkListener() {

			@Override
			public void frameworkEvent(FrameworkEvent event) {
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
		bundleContext().removeFrameworkListener(listener);
	}

	@Override
	protected void register() {
		bundleContext().addFrameworkListener(listener);
	}

	protected List<FrameworkEvent> objects() {
		return objects;
	}



}
