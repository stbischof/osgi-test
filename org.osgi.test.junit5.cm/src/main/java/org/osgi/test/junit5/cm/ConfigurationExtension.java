/*
 * Copyright (c) OSGi Alliance (2020). All Rights Reserved.
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

package org.osgi.test.junit5.cm;

import static org.osgi.test.common.inject.FieldInjector.findAnnotatedFields;
import static org.osgi.test.common.inject.FieldInjector.setField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.test.common.annotation.config.InjectConfiguration;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.common.annotation.config.WithConfigurations;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfigurations;
import org.osgi.test.common.dictionary.Dictionaries;

public class ConfigurationExtension implements BeforeEachCallback, ParameterResolver,
	org.junit.jupiter.api.extension.BeforeAllCallback, AfterAllCallback, AfterEachCallback {

	private static final String							STORE_CONFIGURATION_KEY	= "store.configurationAdmin";
	private BundleContext								bundleContext			= null;
	private ServiceReference<ConfigurationAdmin>		sref					= null;
	private ServiceRegistration<ConfigurationListener>	registrationTimeoutListener;
	private UpdateHandler								timeoutListener;

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {

		storeConfig(extensionContext);

		List<Field> fields = findAnnotatedFields(extensionContext.getRequiredTestClass(), InjectConfiguration.class);
		for (Field field : fields) {

			assertValidFieldCandidate(field);

			InjectConfiguration configAnnotation = field.getAnnotation(InjectConfiguration.class);
			Class<?> memberType = field.getType();
			Type genericMemberType = field.getGenericType();

			setField(field, extensionContext.getRequiredTestInstance(),
				getInjectConfiguration(memberType, genericMemberType, configAnnotation, getConfigAdmin()));

		}
		handleElementAnnotation(extensionContext);

	}

	private void storeConfig(ExtensionContext extensionContext) throws Exception {

		List<Configuration> configurations = ConfigAdminUtil.getAllConfigurations(getConfigAdmin());
		List<ConfigurationCopy> configurationCopies = ConfigAdminUtil.cloneConfigurations(configurations);

		extensionContext.getStore(Namespace.create(ConfigurationExtension.class, extensionContext.getUniqueId()))
			.put(STORE_CONFIGURATION_KEY, configurationCopies);

	}

	static void handleWithConfiguration(WithConfiguration configAnnotation, ConfigurationAdmin configurationAdmin,
		UpdateHandler updateHandler) throws ParameterResolutionException, IllegalArgumentException {

		try {
			Configuration configBefore = ConfigAdminUtil.getConfigsByServicePid(configurationAdmin,
				configAnnotation.pid(), 0l);

			Configuration configuration = configurationAdmin.getConfiguration(configAnnotation.pid());

			updateHandler.extracted(configBefore, configuration, Dictionaries.of(configAnnotation.properties()));

		} catch (Exception e) {
			throw new ParameterResolutionException("ConfigurationAdmin could not be found", e);
		}

	}

	static void handleWithFactoryConfiguration(WithFactoryConfiguration configAnnotation,
		ConfigurationAdmin configurationAdmin, UpdateHandler updateHandler)
		throws ParameterResolutionException, IllegalArgumentException {

		try {

			Configuration configBefore = ConfigAdminUtil.getConfigsByServicePid(configurationAdmin,
				configAnnotation.factoryPid() + "~" + configAnnotation.name());

			Configuration configuration = configurationAdmin.getFactoryConfiguration(configAnnotation.factoryPid(),
				configAnnotation.name());

			updateHandler.extracted(configBefore, configuration, Dictionaries.of(configAnnotation.properties()));
		} catch (Exception e) {
			throw new ParameterResolutionException("ConfigurationAdmin could not be found", e);
		}

	}

	static void assertValidFieldCandidate(Field field) {
		if (Modifier.isFinal(field.getModifiers()) || Modifier.isPrivate(field.getModifiers())
			|| Modifier.isStatic(field.getModifiers())) {
			throw new ExtensionConfigurationException("@" + WithConfiguration.class.getSimpleName() + " field ["
				+ field.getName() + "] must not be final, private or static.");
		}
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
		throws ParameterResolutionException {

		try {

			BundleContext bundleContext = FrameworkUtil.getBundle(extensionContext.getRequiredTestClass())
				.getBundleContext();

			Optional<InjectConfiguration> injectConfiguration = parameterContext
				.findAnnotation(InjectConfiguration.class);
			Parameter parameter = parameterContext.getParameter();

			Class<?> memberType = parameterContext.getParameter()
				.getType();

			Type genericMemberType = null;
			Type t = parameterContext.getParameter()
				.getParameterizedType();
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				if (pt != null) {
					Type[] ts = pt.getActualTypeArguments();
					if (ts != null && ts.length == 1) {
						genericMemberType = ts[0];
					}
				}

			}
			return getInjectConfiguration(memberType, genericMemberType, injectConfiguration.get(), getConfigAdmin());

		} catch (Exception e) {

			throw new ParameterResolutionException("Could not get Configuration from Configuration-Admin", e);
		}
	}

	private Object getInjectConfiguration(Class<?> memberType, Type genericMemberType,
		InjectConfiguration injectConfiguration, ConfigurationAdmin configurationAdmin) throws Exception {

		Configuration configuration = null;

		configuration = ConfigAdminUtil.getConfigsByServicePid(configurationAdmin, injectConfiguration.value(),
			injectConfiguration.timeout());

		if (memberType.equals(Configuration.class)) {
			return configuration;
		} else if (memberType.equals(Optional.class) && Configuration.class.equals(genericMemberType)) {
			return Optional.ofNullable(configuration);
		} else if (memberType.equals(Map.class)) {
			return Dictionaries.asMap(configuration.getProperties());
		} else if (memberType.equals(Dictionary.class)) {
			return configuration.getProperties();
		}
		// TODO: use Converter??
		throw new ParameterResolutionException("Bad Parameter-Type");

	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
		throws ParameterResolutionException {

		if (parameterContext.isAnnotated(InjectConfiguration.class)) {
			Parameter parameter = parameterContext.getParameter();
			if (parameter.getType()
				.isAssignableFrom(Configuration.class)
				|| parameter.getType()
					.isAssignableFrom(Optional.class)
					&& (((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0]
						.equals(Configuration.class))
				|| parameter.getType()
					.isAssignableFrom(Map.class)
				|| parameter.getType()
					.isAssignableFrom(Dictionary.class)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		bundleContext = FrameworkUtil.getBundle(extensionContext.getRequiredTestClass())
			.getBundleContext();

		if (registrationTimeoutListener == null) {
			timeoutListener = new UpdateHandler();
			registrationTimeoutListener = bundleContext.registerService(ConfigurationListener.class, timeoutListener,
				null);
		}
		try {
			sref = bundleContext.getServiceReference(ConfigurationAdmin.class);
			if (sref == null) {
				throw new IllegalStateException("The configuration admin service cannot be found.");
			}

			if (getConfigAdmin() == null) {
				throw new IllegalStateException("The configuration admin service cannot be found.");
			}
		} catch (Exception e) {
			throw new ExtensionConfigurationException(e.getMessage(), e);
		}

		storeConfig(extensionContext);
		handleElementAnnotation(extensionContext);

	}

	private ConfigurationAdmin getConfigAdmin() {

		return bundleContext.getService(sref);
	}

	private void handleElementAnnotation(ExtensionContext extensionContext) {

		extensionContext.getElement()
			.ifPresent((element) -> {
				BundleContext bundleContext = FrameworkUtil.getBundle(extensionContext.getRequiredTestClass())
					.getBundleContext();
				WithConfigurations configAnnotations = element.getAnnotation(WithConfigurations.class);
				if (configAnnotations != null) {
					Stream.of(configAnnotations.value())
						.forEachOrdered((configAnnotation) -> {
							handleWithConfiguration(configAnnotation, getConfigAdmin(), timeoutListener);
						});
				}
				WithConfiguration configAnnotation = element.getAnnotation(WithConfiguration.class);
				if (configAnnotation != null) {
					handleWithConfiguration(configAnnotation, getConfigAdmin(), timeoutListener);
				}

				WithFactoryConfigurations factoryConfigAnnotations = element
					.getAnnotation(WithFactoryConfigurations.class);
				if (factoryConfigAnnotations != null) {
					Stream.of(factoryConfigAnnotations.value())
						.forEachOrdered((factoryConfigAnnotation) -> {
							handleWithFactoryConfiguration(factoryConfigAnnotation, getConfigAdmin(), timeoutListener);
						});
				}
				WithFactoryConfiguration factoryConfigAnnotation = element
					.getAnnotation(WithFactoryConfiguration.class);
				if (factoryConfigAnnotation != null) {
					handleWithFactoryConfiguration(factoryConfigAnnotation, getConfigAdmin(), timeoutListener);
				}
			});
	}

	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
		reset(extensionContext);

	}

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {

		reset(extensionContext);

		if (registrationTimeoutListener != null) {
			registrationTimeoutListener.unregister();
			registrationTimeoutListener = null;
		}
		if (bundleContext != null && sref != null) {
			bundleContext.ungetService(sref);
		}
	}

	private void reset(ExtensionContext extensionContext) throws Exception {
		List<ConfigurationCopy> copys = getStore(extensionContext).get(STORE_CONFIGURATION_KEY, List.class);
		ConfigAdminUtil.resetConfig(timeoutListener, getConfigAdmin(), copys);
		getStore(extensionContext).remove(STORE_CONFIGURATION_KEY, List.class);
	}

	static Store getStore(ExtensionContext extensionContext) {
		return extensionContext
			.getStore(Namespace.create(ConfigurationExtension.class, extensionContext.getUniqueId()));
	}
}
