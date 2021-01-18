package org.osgi.test.junit5.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.test.common.annotation.InjectInstallBundle;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectBundleInstaller;
import org.osgi.test.common.install.BundleInstaller;
import org.osgi.test.common.install.BundleInstaller.EmbeddedLocation;
import org.osgi.test.junit5.testutils.OSGiSoftAssertions;

@ExtendWith(InjectBundleExtension.class)
@ExtendWith(BundleContextExtension.class)
public class BundleInjectionTest {

	// private static final String TB1_JAR_ =
	// "bundle:org.osgi.test.junit5-tests/tb1.jar";

	//
	private static final String	TB1_JAR	= "tb1.jar";

	@InjectBundleInstaller
	static BundleInstaller		bI;

	@InjectInstallBundle(TB1_JAR)
	static volatile Bundle		bundleFieldStatic;

	@InjectInstallBundle(TB1_JAR)
	Bundle						bundleField;

	static OSGiSoftAssertions	staticSoftly;

	OSGiSoftAssertions			softly;

	@BeforeAll
	static void beforeAll(@InjectInstallBundle(TB1_JAR) Bundle bundleParam) {
		assertThat(bI).isNotNull();
		Bundle iBBundle = bI.installBundle(TB1_JAR, false);

		assertThat(bundleFieldStatic).isNotNull();
		staticSoftly = new OSGiSoftAssertions();
		staticSoftly.assertThat(bundleFieldStatic)
			.as("bundleFieldStatic:beforeAll")
			.isNotNull()
			.isSameAs(bundleParam)
			.isSameAs(iBBundle);

		staticSoftly.assertAll();
	}

	@BeforeEach
	void beforeEach(@InjectInstallBundle(TB1_JAR) Bundle bundleParam) {
		Bundle iBBundle = bI.installBundle(TB1_JAR, false);
		assertThat(bundleParam).isNotNull();
		softly = new OSGiSoftAssertions();
		softly.assertThat(bundleParam)
			.as("bundleParam:beforeEach")
			.isNotNull()
			.isSameAs(bundleField)
			.isSameAs(bundleFieldStatic)
			.isSameAs(iBBundle);
		softly.assertAll();
	}

	@Test
	void innerTest(@InjectInstallBundle(TB1_JAR) Bundle bundleParam) {

		assertThat(bI).isNotNull();
		Bundle iBBundle = bI.installBundle(TB1_JAR, false);

		assertThat(bundleParam).isNotNull();
		softly = new OSGiSoftAssertions();
		softly.assertThat(bundleParam)
			.as("bundleParam:innerTest")
			.isNotNull()
			.isSameAs(bundleField)
			.isSameAs(bundleFieldStatic)
			.isSameAs(iBBundle);
		softly.assertAll();
	}

	@Test
	void embeddedLocationTest(@InjectBundleContext BundleContext bundleContext) throws IOException {

		EmbeddedLocation eLoc = BundleInstaller.EmbeddedLocation.of(bundleContext, TB1_JAR);
		assertThat(eLoc).isNotNull();

		assertThat(eLoc.openStream(bundleContext)).isNotNull()
			.isNotEmpty();
		EmbeddedLocation eLocEx = BundleInstaller.EmbeddedLocation.of("unknown", Version.valueOf("1.1.1"), "/",
			"unknown.jar");
		assertThat(eLocEx).isNotNull();
		assertThatThrownBy(() -> eLocEx.openStream(bundleContext)).isInstanceOf(IllegalArgumentException.class);
	}

	@AfterEach
	void afterEach(@InjectInstallBundle(TB1_JAR) Bundle bundleParam) {
		assertThat(bI).isNotNull();
		Bundle iBBundle = bI.installBundle(TB1_JAR, false);
		softly = new OSGiSoftAssertions();
		softly.assertThat(bundleParam)
			.as("afterEach")
			.isNotNull()
			.isSameAs(bundleField)
			.isSameAs(bundleFieldStatic)
			.isSameAs(iBBundle);
		softly.assertAll();
	}

	@AfterAll
	static void afterAll(@InjectInstallBundle(TB1_JAR) Bundle bundleParam) {

		assertThat(bI).isNotNull();
		Bundle iBBundle = bI.installBundle(TB1_JAR, false);

		staticSoftly = new OSGiSoftAssertions();
		staticSoftly.assertThat(bundleParam)
			.as("afterAll")
			.isNotNull()
			.isSameAs(iBBundle)
			.isSameAs(bundleFieldStatic);
		staticSoftly.assertAll();
	}
}
