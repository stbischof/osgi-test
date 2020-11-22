/*
 * Copyright (c) OSGi Alliance (2019, 2020). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgi.test.junit5.event;

import static org.osgi.test.common.inject.FieldInjector.findAnnotatedFields;
import static org.osgi.test.common.inject.FieldInjector.findAnnotatedNonStaticFields;
import static org.osgi.test.common.inject.FieldInjector.setField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.test.common.annotation.InjectEventListener;
import org.osgi.test.common.inject.TargetType;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.event.store.EventStore;
import org.osgi.test.junit5.event.store.EventStoreImpl;

public class EventListenerExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {
	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		List<Field> fields = findAnnotatedFields(extensionContext.getRequiredTestClass(), InjectEventListener.class,
			m -> Modifier.isStatic(m.getModifiers()));
		fields.forEach(field -> {
			assertValidFieldCandidate(field);
			InjectEventListener injectListener = field.getAnnotation(InjectEventListener.class);
			TargetType targetType = TargetType.of(field);
			setField(field, null, resolveInjectEventListenerReturnValue(targetType, injectListener, extensionContext));
		});
	}

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		for (Object instance : extensionContext.getRequiredTestInstances()
			.getAllInstances()) {
			List<Field> fields = findAnnotatedNonStaticFields(instance.getClass(), InjectEventListener.class);
			fields.forEach(field -> {
				assertValidFieldCandidate(field);
				InjectEventListener injectListener = field.getAnnotation(InjectEventListener.class);
				TargetType targetType = TargetType.of(field);
				setField(field, instance,
					resolveInjectEventListenerReturnValue(targetType, injectListener, extensionContext));
			});
		}
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
		throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		TargetType targetType = TargetType.of(parameter);
		Optional<InjectEventListener> injectListener = parameterContext.findAnnotation(InjectEventListener.class);
		if (injectListener.isPresent()) {
			return resolveInjectEventListenerReturnValue(targetType, injectListener.get(), extensionContext);
		}
		throw new ParameterResolutionException("Unspported Parameter");
	}

	private Object resolveInjectEventListenerReturnValue(TargetType targetType, InjectEventListener injectListener,
		ExtensionContext extensionContext) {
		String providerMethod = injectListener.providerMethod();
		Object listener = null;
		if (providerMethod.isEmpty()) {
			listener = serviceListenerFromTargetClass(targetType, injectListener, extensionContext);
		} else {
			Optional<Object> oInstance = extensionContext.getTestInstance();
			if (oInstance.isPresent()) {
				listener = invokeServiceListener(extensionContext, providerMethod, oInstance.get()
					.getClass(), oInstance);
			} else {
				Optional<Class<?>> oClass = extensionContext.getTestClass();
				if (oClass.isPresent()) {
					listener = invokeServiceListener(extensionContext, providerMethod, oClass.get(), Optional.empty());
				}
			}
		}
		BundleContext bc = BundleContextExtension.getBundleContext(extensionContext);
		try {
			if (listener instanceof ServiceListener) {
				String filter = injectListener.filter()
					.isEmpty() ? null : injectListener.filter();
				bc.addServiceListener((ServiceListener) listener, filter);
			}
			if (listener instanceof BundleListener) {
				bc.addBundleListener((BundleListener) listener);
			}
			if (listener instanceof FrameworkListener) {
				bc.addFrameworkListener((FrameworkListener) listener);
			}
		} catch (InvalidSyntaxException e) {
			throw new ParameterResolutionException(
				String.format("Wrong filter-syntax in @injectListener", targetType.getType()));
		}
		final Object fListener = listener;
		getStore(extensionContext).getOrComputeIfAbsent(UUID.randomUUID(),
			uuid -> new CloseableServiceListener(bc, fListener));
		return fListener;
	}

	private Object invokeServiceListener(ExtensionContext extensionContext, String providerMethod, Class<?> clazz,
		Optional<Object> oInstance) {
		Optional<Object> listener = Stream.of(clazz.getMethods())
			.filter(m -> !Modifier.isAbstract(m.getModifiers()))
			.filter(m -> m.getName()
				.equals(providerMethod))
			.findFirst()
			.map(m -> {
				try {
					if (m.getParameterCount() == 0) {
						if (Modifier.isStatic(m.getModifiers())) {
							return m.invoke(null);
						} else if (oInstance.isPresent()) {
							return m.invoke(oInstance.get());
						}
					}
					if (m.getParameterCount() == 1
						&& TestInfo.class.equals(m.getParameterTypes()[0])) {
						TestInfo testInfo = new TestInfo() {

							@Override
							public Optional<Method> getTestMethod() {

								return extensionContext.getTestMethod();
							}

							@Override
							public Optional<Class<?>> getTestClass() {
								return extensionContext.getTestClass();
							}

							@Override
							public Set<String> getTags() {
								return extensionContext.getTags();
							}

							@Override
							public String getDisplayName() {
								return extensionContext.getDisplayName();
							}
						};
						if (Modifier.isStatic(m.getModifiers())) {
							return m.invoke(null, testInfo);
						} else if (oInstance.isPresent()) {
							return m.invoke(oInstance.get(), testInfo);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return null;
			});
		return listener.orElseThrow(() -> new ParameterResolutionException(
			String.format("No provider-method with name `%s` found.", providerMethod)));
	}

	private Object serviceListenerFromTargetClass(TargetType targetType, InjectEventListener injectListener,
		ExtensionContext extensionContext) {
		try {
			if (targetType.getType()
				.equals(EventStore.class)) {
				return new EventStoreImpl(BundleContextExtension.getBundleContext(extensionContext));
			}
			Optional<Constructor<?>> oConstructor = parameterlessPublicConstructor(targetType.getType());
			if (oConstructor.isPresent()) {
				Object listener = oConstructor.get()
					.newInstance();
				return listener;
			}
			throw new ParameterResolutionException(
				String.format("The class `%s` has no public no-arg Constructor ", targetType.getType()));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			| InvocationTargetException e) {
			throw new ParameterResolutionException(
				String.format("Could not create newInstance from ServiceListener of type `%s`", targetType.getType()),
				e);
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
		throws ParameterResolutionException {
		Optional<InjectEventListener> oAnn = parameterContext.findAnnotation(InjectEventListener.class);
		if (oAnn.isPresent()) {
			InjectEventListener injectListener = oAnn.get();
			TargetType targetType = TargetType.of(parameterContext.getParameter());
			if (targetType.getType()
				.equals(EventStore.class)
				|| !injectListener.providerMethod()
					.isEmpty()
				|| parameterlessPublicConstructor(targetType.getType()).isPresent()) {
				return true;
			}
		}
		return false;
	}

	private Optional<Constructor<?>> parameterlessPublicConstructor(Class<?> clazz) {
		for (Constructor<?> constructor : clazz.getConstructors()) {
			if (constructor.getParameterCount() == 0) {
				return Optional.of(constructor);
			}
		}
		return Optional.empty();
	}

	static void assertValidFieldCandidate(Field field) {
		if (Modifier.isFinal(field.getModifiers()) || Modifier.isPrivate(field.getModifiers())) {
			throw new ExtensionConfigurationException("@" + InjectEventListener.class.getSimpleName() + " field ["
				+ field.getName() + "] must not be final or private.");
		}
	}

	static Store getStore(ExtensionContext extensionContext) {
		return extensionContext
			.getStore(Namespace.create(EventListenerExtension.class, extensionContext.getUniqueId()));
	}

	public static class CloseableServiceListener implements CloseableResource {
		private final Object		listener;
		private final BundleContext	bundleContext;

		CloseableServiceListener(BundleContext bundleContext, Object listener) {
			this.listener = listener;
			this.bundleContext = bundleContext;
		}

		@Override
		public void close() throws Exception {
			if (listener instanceof ServiceListener) {
				bundleContext.removeServiceListener((ServiceListener) listener);
			}
			if (listener instanceof BundleListener) {
				bundleContext.removeBundleListener((BundleListener) listener);
			}
			if (listener instanceof FrameworkListener) {
				bundleContext.removeFrameworkListener((FrameworkListener) listener);
			}
		}
	}
}
