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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundle;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectInstallBundle;
import org.osgi.test.common.install.InstallBundle;

public class InjectBundleExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		List<Field>

		fields = findAnnotatedFields(extensionContext.getRequiredTestClass(), InjectBundle.class,
			m -> Modifier.isStatic(m.getModifiers()));

		fields.forEach(field -> {
			assertFieldIsOfType(field, Bundle.class, InjectBundle.class, ExtensionConfigurationException::new);
			InjectBundle injectBundle = field.getAnnotation(InjectBundle.class);
			setField(field, null, getBundle(extensionContext, injectBundle));
		});
	}

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		for (Object instance : extensionContext.getRequiredTestInstances()
			.getAllInstances()) {
			final Class<?> testClass = instance.getClass();

			List<Field> fields = findAnnotatedNonStaticFields(testClass, InjectBundle.class);

			fields.forEach(field -> {
				assertFieldIsOfType(field, Bundle.class, InjectBundle.class, ExtensionConfigurationException::new);
				InjectBundle injectBundle = field.getAnnotation(InjectBundle.class);
				setField(field, instance, getBundle(extensionContext, injectBundle));
			});
		}
	}

	/**
	 * Resolve {@link Parameter} annotated with
	 * {@link InjectBundleContext @BundleContextParameter} OR
	 * {@link InjectInstallBundle @InstallBundleParameter} in the supplied
	 * {@link ParameterContext}.
	 */
	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();

		if (parameterContext.isAnnotated(InjectBundle.class)) {
			assertParameterIsOfType(parameterType, Bundle.class, InjectBundle.class, ParameterResolutionException::new);
			InjectBundle injectBundle = parameter.getAnnotation(InjectBundle.class);
			return getBundle(extensionContext, injectBundle);
		}

		throw new ExtensionConfigurationException("No parameter types known to BundleContextExtension were found");
	}

	/**
	 * Determine if the {@link Parameter} in the supplied
	 * {@link ParameterContext} is annotated with
	 * {@link InjectBundleContext @BundleContextParameter} OR
	 * {@link InjectInstallBundle @InstallBundleParameter}.
	 */
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		boolean annotatedBundleParameter = parameterContext.isAnnotated(InjectBundle.class);

		if (annotatedBundleParameter && (parameterContext.getDeclaringExecutable() instanceof Constructor)) {
			throw new ParameterResolutionException(
				"BundleContextExtension does not support parameter injection on constructors");
		}
		return annotatedBundleParameter;
	}

	private Bundle getBundle(ExtensionContext extensionContext, InjectBundle injectBundle) {

		return installBundleOf(injectBundle, extensionContext);
		// TODO:: ASK BJ extra store? Should work with InnstallBundle only.
		// return getStore(extensionContext).getOrComputeIfAbsent(injectBundle,
		// key -> installBundleOf(key, extensionContext), Bundle.class);

	}

	private Bundle installBundleOf(InjectBundle injectBundle, ExtensionContext extensionContext) {

		try {
			BundleContext bc = BundleContextExtension.getBundleContext(extensionContext);
			InstallBundle ib = BundleContextExtension.getInstallBundle(extensionContext);

			String spec = injectBundle.value();
			if (spec.startsWith("http:") || spec.startsWith("https:") || spec.startsWith("file:")) {
				return ib.installBundle(new URL(injectBundle.value()), injectBundle.start());
			} else {
				return ib.installBundle(InstallBundle.EmbeddedLocation.of(bc, spec), injectBundle.start());
			}
		} catch (MalformedURLException e) {
			throw new ExtensionConfigurationException(
				String.format("Could not parse URL from given String %s.", injectBundle.value()), e);
		}

	}

	static Store getStore(ExtensionContext extensionContext) {
		return extensionContext.getStore(Namespace.create(InjectBundleExtension.class, extensionContext.getUniqueId()));
	}

}
