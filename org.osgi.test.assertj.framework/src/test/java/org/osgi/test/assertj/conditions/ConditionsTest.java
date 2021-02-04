package org.osgi.test.assertj.conditions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.test.assertj.testutil.ConditionAssert;
import org.osgi.test.common.bitmaps.BundleEventType;
import org.osgi.test.common.bitmaps.FrameworkEventType;
import org.osgi.test.common.bitmaps.ServiceEventType;
import org.osgi.test.common.dictionary.Dictionaries;

public class ConditionsTest implements ConditionAssert {

	@Nested
	class BundleEventConditions {

		Bundle		bundle;
		BundleEvent	bundleEvent;
		Bundle		otherbundle;

		@BeforeEach
		private void beforEach() {
			bundleEvent = mock(BundleEvent.class, "theBundleEvent");
			bundle = mock(Bundle.class, "theBundle");
			otherbundle = mock(Bundle.class, "otherbundle");
		}

		@Test
		void bundleEquals() throws Exception {

			when(bundleEvent.getBundle()).thenReturn(bundle);
			passingHas(Conditions.BundleEventConditions.bundleEquals(bundle), bundleEvent);

			failingHas(Conditions.BundleEventConditions.bundleEquals(otherbundle), bundleEvent, "bundle equals <%s>",
				otherbundle);
		}

		@Test
		void bundleIsNotNull() throws Exception {

			failingHas(Conditions.BundleEventConditions.bundleIsNotNull(), bundleEvent, "bundle equals <null>");

			when(bundleEvent.getBundle()).thenReturn(bundle);
			passingHas(Conditions.BundleEventConditions.bundleIsNotNull(), bundleEvent);
		}

		@Test
		void bundleIsNull() throws Exception {

			passingHas(Conditions.BundleEventConditions.bundleIsNull(), bundleEvent);

			when(bundleEvent.getBundle()).thenReturn(bundle);
			failingHas(Conditions.BundleEventConditions.bundleIsNull(), bundleEvent, "bundle equals <null>");
		}

		@Test
		void type() throws Exception {

			when(bundleEvent.getType()).thenReturn(BundleEvent.INSTALLED);
			passingHas(Conditions.BundleEventConditions.type(BundleEvent.INSTALLED), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.UPDATED);
			failingHas(Conditions.BundleEventConditions.type(BundleEvent.INSTALLED), bundleEvent,
				"type matches mask <%s>", BundleEventType.BITMAP.maskToString(BundleEvent.INSTALLED));

			when(bundleEvent.getType()).thenReturn(BundleEvent.INSTALLED);
			passingHas(Conditions.BundleEventConditions.typeInstalled(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.LAZY_ACTIVATION);
			passingHas(Conditions.BundleEventConditions.typeLazyActivation(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.RESOLVED);
			passingHas(Conditions.BundleEventConditions.typeResolved(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.STARTED);
			passingHas(Conditions.BundleEventConditions.typeStarted(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.STARTING);
			passingHas(Conditions.BundleEventConditions.typeStarting(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.STOPPED);
			passingHas(Conditions.BundleEventConditions.typeStopped(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.STOPPING);
			passingHas(Conditions.BundleEventConditions.typeStopping(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.UNINSTALLED);
			passingHas(Conditions.BundleEventConditions.typeUninstalled(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.UNRESOLVED);
			passingHas(Conditions.BundleEventConditions.typeUnresolved(), bundleEvent);

			when(bundleEvent.getType()).thenReturn(BundleEvent.UPDATED);
			passingHas(Conditions.BundleEventConditions.typeUpdated(), bundleEvent);

		}
	}

	@Nested
	class DictionaryConditions {
		String	k1	= "k1";
		String	k2	= "k2";
		String	v1	= "v1";
		String	v2	= "v2";

		@Test
		void serviceProperties() throws Exception {
			passingHas(Conditions.DictionaryConditions.servicePropertiesContained(Dictionaries.dictionaryOf(k1, v1)),
				Dictionaries.dictionaryOf(k1, v1));
			passingHas(Conditions.DictionaryConditions.servicePropertiesContained(
				Dictionaries.asMap(Dictionaries.dictionaryOf(k1, v1))), Dictionaries.dictionaryOf(k1, v1));

			failingHas(Conditions.DictionaryConditions.servicePropertiesContained(Dictionaries.dictionaryOf(k2, v2)),
				Dictionaries.dictionaryOf(k1, v1));
			failingHas(Conditions.DictionaryConditions.servicePropertiesContained(
				Dictionaries.asMap(Dictionaries.dictionaryOf(k2, v2))), Dictionaries.dictionaryOf(k1, v1));
		}

		@Test
		void serviceProperty() throws Exception {
			passingHas(Conditions.DictionaryConditions.servicePropertyContained(k1, v1),
				Dictionaries.dictionaryOf(k1, v1));

			failingHas(Conditions.DictionaryConditions.servicePropertyContained(k2, v2),
				Dictionaries.dictionaryOf(k1, v1));
		}
	}

	@Nested
	class FrameworkEventConditions {

		Bundle			bundle;
		FrameworkEvent	frameworkEvent;
		Bundle			otherbundle;
		Throwable		throwable;

		@BeforeEach
		private void beforEach() {
			frameworkEvent = mock(FrameworkEvent.class, "theFrameworkEvent");
			bundle = mock(Bundle.class, "theBundle");
			otherbundle = mock(Bundle.class, "otherbundle");
			throwable = new NullPointerException("NullPointerException");
		}

		@Test
		void bundleIsNotNull() throws Exception {

			failingHas(Conditions.FrameworkEventConditions.bundleIsNotNull(), frameworkEvent, "bundle equals <null>");

			when(frameworkEvent.getBundle()).thenReturn(bundle);
			passingHas(Conditions.FrameworkEventConditions.bundleIsNotNull(), frameworkEvent);
		}

		@Test
		void throwableIsNotNull() throws Exception {

			failingHas(Conditions.FrameworkEventConditions.throwableIsNotNull(), frameworkEvent,
				"throwable is <null>");

			when(frameworkEvent.getThrowable()).thenReturn(throwable);
			passingHas(Conditions.FrameworkEventConditions.throwableIsNotNull(), frameworkEvent);

		}

		@Test
		void throwableIsNull() throws Exception {

			passingHas(Conditions.FrameworkEventConditions.throwableIsNull(), frameworkEvent);

			when(frameworkEvent.getThrowable()).thenReturn(throwable);
			failingHas(Conditions.FrameworkEventConditions.throwableIsNull(), frameworkEvent,
				"throwable is <null>");
		}

		@Test
		void throwableOfClass() throws Exception {

			when(frameworkEvent.getThrowable()).thenReturn(throwable);

			passingHas(Conditions.FrameworkEventConditions.throwableOfClass(NullPointerException.class),
				frameworkEvent);

			failingHas(Conditions.FrameworkEventConditions.throwableOfClass(IllegalArgumentException.class),
				frameworkEvent, "throwable of Class");// TODO: fix assertj
			// frameworkEvent, "throwable of Class <%s>",
			// IllegalArgumentException.class);
		}

		@Test
		void type() throws Exception {
			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.ERROR);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.ERROR), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeError(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.INFO);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.INFO), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeInfo(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.PACKAGES_REFRESHED);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.PACKAGES_REFRESHED), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typePackagesRefreshed(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.STARTED);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.STARTED), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeStarted(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.STARTLEVEL_CHANGED);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.STARTLEVEL_CHANGED), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeStartLevelChanged(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.STOPPED);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.STOPPED), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeStopped(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.STOPPED_BOOTCLASSPATH_MODIFIED);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.STOPPED_BOOTCLASSPATH_MODIFIED),
				frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeStopped_BootClasspathModified(), frameworkEvent);

			// TODO: R7
			// when(frameworkEvent.getType()).thenReturn(FrameworkEvent.STOPPED_SYSTEM_REFRESHED);
			// passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.STOPPED_SYSTEM_REFRESHED),
			// frameworkEvent);
			// passingHas(Conditions.FrameworkEventConditions.typeStoppedSystemRefreshes(),
			// frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.STOPPED_UPDATE);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.STOPPED_UPDATE), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeStoppedUpdate(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.WAIT_TIMEDOUT);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.WAIT_TIMEDOUT), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeWaitTimeout(), frameworkEvent);

			when(frameworkEvent.getType()).thenReturn(FrameworkEvent.WARNING);
			passingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.WARNING), frameworkEvent);
			passingHas(Conditions.FrameworkEventConditions.typeWarning(), frameworkEvent);

			failingHas(Conditions.FrameworkEventConditions.type(FrameworkEvent.INFO), frameworkEvent,
				"type matches mask <%s>", FrameworkEventType.BITMAP.maskToString(FrameworkEvent.INFO));
		}

	}

	@Nested
	class ServiceEventConditions {

		@SuppressWarnings("rawtypes")
		ServiceReference	otherServiceReference;

		ServiceEvent		serviceEvent;

		@SuppressWarnings("rawtypes")
		ServiceReference	serviceReference;

		@BeforeEach
		private void beforEach() {
			serviceEvent = mock(ServiceEvent.class, "theServiceEvent");
			serviceReference = mock(ServiceReference.class, "serviceReference");
			otherServiceReference = mock(ServiceReference.class, "otherServiceReference");
		}

		@SuppressWarnings("unchecked")
		@Test
		void serviceReferenceEquals() throws Exception {

			when(serviceEvent.getServiceReference()).thenReturn(serviceReference);
			passingHas(Conditions.ServiceEventConditions.serviceReferenceEquals(serviceReference), serviceEvent);

			failingHas(Conditions.ServiceEventConditions.serviceReferenceEquals(otherServiceReference), serviceEvent,
				"serviceReference equals <%s>", otherServiceReference);
		}

		@SuppressWarnings("unchecked")
		@Test
		void serviceReferenceHas() throws Exception {
			Condition<ServiceReference<?>> c = Conditions.ServiceReferenceConditions
				.sameAs(mock(ServiceReference.class));

			when(serviceEvent.getServiceReference()).thenReturn(serviceReference);
			failingHas(Conditions.ServiceEventConditions.serviceReferenceHas(c), serviceEvent,
				"serviceReference equals");

			c = Conditions.ServiceReferenceConditions.sameAs(serviceReference);
			passingHas(Conditions.ServiceEventConditions.serviceReferenceHas(c), serviceEvent);

		}

		@SuppressWarnings("unchecked")
		@Test
		void serviceReferenceIsNotNull() throws Exception {

			failingHas(Conditions.ServiceEventConditions.serviceReferenceIsNotNull(), serviceEvent,
				"not.*serviceReference is <null>");

			when(serviceEvent.getServiceReference()).thenReturn(serviceReference);
			passingHas(Conditions.ServiceEventConditions.serviceReferenceIsNotNull(), serviceEvent);
		}

		@SuppressWarnings("unchecked")
		@Test
		void serviceReferenceIsNull() throws Exception {

			passingHas(Conditions.ServiceEventConditions.serviceReferenceIsNull(), serviceEvent);

			when(serviceEvent.getServiceReference()).thenReturn(serviceReference);
			failingHas(Conditions.ServiceEventConditions.serviceReferenceIsNull(), serviceEvent,
				"serviceReference is <null>");
		}

		@Test
		void type() throws Exception {

			when(serviceEvent.getType()).thenReturn(ServiceEvent.MODIFIED);
			passingHas(Conditions.ServiceEventConditions.type(ServiceEvent.MODIFIED), serviceEvent);

			when(serviceEvent.getType()).thenReturn(ServiceEvent.MODIFIED_ENDMATCH);
			failingHas(Conditions.ServiceEventConditions.type(ServiceEvent.MODIFIED), serviceEvent,
				"type matches mask <%s>", ServiceEventType.BITMAP.maskToString(ServiceEvent.MODIFIED));

			when(serviceEvent.getType()).thenReturn(ServiceEvent.MODIFIED);
			passingHas(Conditions.ServiceEventConditions.typeModified(), serviceEvent);
			failingHas(Conditions.ServiceEventConditions.typeModifiedEndmatch(), serviceEvent, "type matches mask <%s>",
				ServiceEventType.BITMAP.maskToString(ServiceEvent.MODIFIED_ENDMATCH));

			when(serviceEvent.getType()).thenReturn(ServiceEvent.MODIFIED_ENDMATCH);
			passingHas(Conditions.ServiceEventConditions.typeModifiedEndmatch(), serviceEvent);
			failingHas(Conditions.ServiceEventConditions.typeModified(), serviceEvent, "type matches mask <%s>",
				ServiceEventType.BITMAP.maskToString(ServiceEvent.MODIFIED));

			when(serviceEvent.getType()).thenReturn(ServiceEvent.REGISTERED);
			passingHas(Conditions.ServiceEventConditions.typeRegistered(), serviceEvent);
			failingHas(Conditions.ServiceEventConditions.typeUnregistering(), serviceEvent, "type matches mask <%s>",
				ServiceEventType.BITMAP.maskToString(ServiceEvent.UNREGISTERING));

			when(serviceEvent.getType()).thenReturn(ServiceEvent.UNREGISTERING);
			passingHas(Conditions.ServiceEventConditions.typeUnregistering(), serviceEvent);
			failingHas(Conditions.ServiceEventConditions.typeRegistered(), serviceEvent, "type matches mask <%s>",
				ServiceEventType.BITMAP.maskToString(ServiceEvent.REGISTERED));

		}

	}

	@Nested
	class ServiceReferenceConditions {

		class A {}

		@SuppressWarnings("rawtypes")
		ServiceReference	otherServiceReference;

		@SuppressWarnings("rawtypes")
		ServiceReference	serviceReference;

		@BeforeEach
		private void beforEach() {
			serviceReference = mock(ServiceReference.class, "serviceReference");
			otherServiceReference = mock(ServiceReference.class, "otherServiceReference");
		}

		@SuppressWarnings("unchecked")
		@Test
		void serviceReferenceIsNotNull() throws Exception {

			when(serviceReference.getProperty(Constants.OBJECTCLASS)).thenReturn(new String[] {
				A.class.getName()
			});

			passingHas(Conditions.ServiceReferenceConditions.objectClass(A.class), serviceReference);
			failingHas(Conditions.ServiceReferenceConditions.objectClass(A.class), otherServiceReference)
				.hasMessageMatching((regex_startWith_Expecting + "has Objectclass .*"));

		}
	}

	static String	BAR	= "bar";

	static String	FOO	= "foo";

}
