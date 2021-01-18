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

package org.osgi.test.junit5.context;

import static org.osgi.test.common.inject.FieldInjector.assertFieldIsOfType;
import static org.osgi.test.common.inject.FieldInjector.assertParameterIsOfType;
import static org.osgi.test.common.inject.FieldInjector.findAnnotatedFields;
import static org.osgi.test.common.inject.FieldInjector.findAnnotatedNonStaticFields;
import static org.osgi.test.common.inject.FieldInjector.setField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;

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
import org.osgi.framework.FrameworkUtil;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectBundleInstaller;
import org.osgi.test.common.context.CloseableBundleContext;
import org.osgi.test.common.install.BundleInstaller;

/**
 * A JUnit 5 Extension to get the OSGi {@link BundleContext} of the test bundle.
 * <p>
 * The {@link BundleContext} implementation provided by this rule will
 * automatically clean up all service registrations, bundle, service and
 * framework listeners, as well as installed bundles left behind.
 * <p>
 * Example:
 *
 * <pre>
 * &#64;ExtendWith(BundleContextExtension.class)
 * class MyTests {
 *
 * 	&#64;InjectBundleContext
 * 	BundleContext bundleContext;
 *
 * 	&#64;Test
 * 	public void aTest() {
 * 		Bundle bundle = bundleContext.getBundle();
 * 	}
 * }
 * </pre>
 */
public class BundleContextExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

	public static final String	BUNDLE_CONTEXT_KEY	= "bundle.context";
	public static final String	INSTALL_BUNDLE_KEY	= "install.bundle";

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		List<Field> fields = findAnnotatedFields(extensionContext.getRequiredTestClass(), InjectBundleContext.class,
			m -> Modifier.isStatic(m.getModifiers()));

		fields.forEach(field -> {
			assertFieldIsOfType(field, BundleContext.class, InjectBundleContext.class,
				ExtensionConfigurationException::new);
			setField(field, null, getBundleContext(extensionContext));
		});

		fields = findAnnotatedFields(extensionContext.getRequiredTestClass(), InjectBundleInstaller.class,
			m -> Modifier.isStatic(m.getModifiers()));

		fields.forEach(field -> {
			assertFieldIsOfType(field, BundleInstaller.class, InjectBundleInstaller.class,
				ExtensionConfigurationException::new);
			setField(field, null, getInstallBundle(extensionContext));
		});

	}

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		for (Object instance : extensionContext.getRequiredTestInstances()
			.getAllInstances()) {
			final Class<?> testClass = instance.getClass();
			List<Field> fields = findAnnotatedNonStaticFields(testClass, InjectBundleContext.class);

			fields.forEach(field -> {
				assertFieldIsOfType(field, BundleContext.class, InjectBundleContext.class,
					ExtensionConfigurationException::new);
				setField(field, instance, getBundleContext(extensionContext));
			});

			fields = findAnnotatedNonStaticFields(testClass, InjectBundleInstaller.class);

			fields.forEach(field -> {
				assertFieldIsOfType(field, BundleInstaller.class, InjectBundleInstaller.class,
					ExtensionConfigurationException::new);
				setField(field, instance, getInstallBundle(extensionContext));
			});

		}
	}

	/**
	 * Resolve {@link Parameter} annotated with
	 * {@link InjectBundleContext @BundleContextParameter} OR
	 * {@link InjectBundleInstaller @InstallBundleParameter} in the supplied
	 * {@link ParameterContext}.
	 */
	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();

		if (parameterContext.isAnnotated(InjectBundleContext.class)) {
			assertParameterIsOfType(parameterType, BundleContext.class, InjectBundleContext.class,
				ParameterResolutionException::new);
			return getBundleContext(extensionContext);
		} else if (parameterContext.isAnnotated(InjectBundleInstaller.class)) {
			assertParameterIsOfType(parameterType, BundleInstaller.class, InjectBundleInstaller.class,
				ParameterResolutionException::new);
			return getInstallBundle(extensionContext);
		}

		throw new ExtensionConfigurationException("No parameter types known to BundleContextExtension were found");
	}

	/**
	 * Determine if the {@link Parameter} in the supplied
	 * {@link ParameterContext} is annotated with
	 * {@link InjectBundleContext @BundleContextParameter} OR
	 * {@link InjectBundleInstaller @InstallBundleParameter}.
	 */
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		boolean annotatedBundleContextParameter = parameterContext.isAnnotated(InjectBundleContext.class);
		boolean annotatedInstallBundleParameter = parameterContext.isAnnotated(InjectBundleInstaller.class);

		if ((annotatedBundleContextParameter || annotatedInstallBundleParameter)
			&& (parameterContext.getDeclaringExecutable() instanceof Constructor)) {
			throw new ParameterResolutionException(
				"BundleContextExtension does not support parameter injection on constructors");
		}
		return annotatedBundleContextParameter || annotatedInstallBundleParameter;
	}

	public static BundleContext getBundleContext(ExtensionContext extensionContext) {
		BundleContext bundleContext = getStore(extensionContext)
			.getOrComputeIfAbsent(BUNDLE_CONTEXT_KEY,
				key -> new CloseableResourceBundleContext(getParentBundleContext(extensionContext)),
				CloseableResourceBundleContext.class)
			.get();
		return bundleContext;
	}

	private static BundleContext getParentBundleContext(ExtensionContext extensionContext) {
		BundleContext parentContext = extensionContext.getParent()
			.filter(context -> context.getTestClass()
				.isPresent())
			.map(BundleContextExtension::getBundleContext)
			.orElseGet(() -> FrameworkUtil.getBundle(extensionContext.getRequiredTestClass())
				.getBundleContext());
		return parentContext;
	}

	public static BundleInstaller getInstallBundle(ExtensionContext extensionContext) {
		return getStore(extensionContext).getOrComputeIfAbsent(INSTALL_BUNDLE_KEY,
			key -> new BundleInstaller(getBundleContext(extensionContext)), BundleInstaller.class);
	}

	public static class CloseableResourceBundleContext implements CloseableResource {

		private final BundleContext bundleContext;

		CloseableResourceBundleContext(BundleContext bundleContext) {
			this.bundleContext = CloseableBundleContext.proxy(bundleContext);
		}

		@Override
		public void close() throws Exception {
			((AutoCloseable) get()).close();
		}

		public BundleContext get() {
			return bundleContext;
		}

		@Override
		public String toString() {
			return get().toString();
		}
	}

	static Store getStore(ExtensionContext extensionContext) {
		return extensionContext
			.getStore(Namespace.create(BundleContextExtension.class, extensionContext.getUniqueId()));
	}

}
