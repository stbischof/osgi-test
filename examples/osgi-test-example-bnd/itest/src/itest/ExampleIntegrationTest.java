package itest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.dictionary.Dictionaries;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
public class ExampleIntegrationTest {

	@InjectBundleContext
	protected static BundleContext context;

	@BeforeAll
	public static void doBeforeAll() {

		context.registerService(A.class, new A() {
		}, Dictionaries.dictionaryOf("a", "1"));

		context.registerService(A.class, new A() {
		}, Dictionaries.dictionaryOf("a", "2"));

	}

	@AfterAll
	public static void doAfterAll() {

	}

	@BeforeEach
	public void doBeforeEach() {

	}

	@AfterEach
	public void doAfterEach() {

	}

	@Test
	public void testExample(@InjectService ConfigurationAdmin ca) {

		assertNotNull(ca);
	}

	@Test
	public void testExample(@InjectService(cardinality = 1, timeout = 200) ServiceAware<A> a) {

		System.out.println("Hello World");
		assertNotNull(a);
	}

	interface A {
	}
}