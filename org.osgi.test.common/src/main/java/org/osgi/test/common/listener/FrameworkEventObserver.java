package org.osgi.test.common.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public abstract class FrameworkEventObserver<E> extends AbstracEventObserver<E> {
	private FrameworkListener		listener;

	private List<FrameworkEvent>	objects	= new ArrayList<>();

	public FrameworkEventObserver(BundleContext bundleContext, Predicate<FrameworkEvent> matches, int count,
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

	public static class Multiple extends FrameworkEventObserver<List<FrameworkEvent>> {

		public Multiple(BundleContext bundleContext, Predicate<FrameworkEvent> matches, int count, boolean immidiate) {
			super(bundleContext, matches, count, immidiate);
		}

		@Override
		protected List<FrameworkEvent> getResultObject() {
			return objects();
		}

	}

	public static class Single extends FrameworkEventObserver<Optional<FrameworkEvent>> {

		public Single(BundleContext bundleContext, Predicate<FrameworkEvent> matches, boolean immidiate) {
			super(bundleContext, matches, 1, immidiate);
		}

		@Override
		protected Optional<FrameworkEvent> getResultObject() {
			return objects().isEmpty() ? Optional.empty() : Optional.of(objects().get(0));
		}

	}

}
