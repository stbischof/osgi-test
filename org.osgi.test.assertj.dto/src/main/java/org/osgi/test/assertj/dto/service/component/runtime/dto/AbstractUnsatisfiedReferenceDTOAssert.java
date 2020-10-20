package org.osgi.test.assertj.dto.service.component.runtime.dto;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Objects;
import org.osgi.service.component.runtime.dto.UnsatisfiedReferenceDTO;

/**
 * Abstract base class for {@link UnsatisfiedReferenceDTO} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractUnsatisfiedReferenceDTOAssert<S extends AbstractUnsatisfiedReferenceDTOAssert<S, A>, A extends UnsatisfiedReferenceDTO> extends AbstractObjectAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractUnsatisfiedReferenceDTOAssert}</code> to make assertions on actual UnsatisfiedReferenceDTO.
   * @param actual the UnsatisfiedReferenceDTO we want to make assertions on.
   */
  protected AbstractUnsatisfiedReferenceDTOAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual UnsatisfiedReferenceDTO's name is equal to the given one.
   * @param name the given name to compare the actual UnsatisfiedReferenceDTO's name to.
   * @return this assertion object.
   * @throws AssertionError - if the actual UnsatisfiedReferenceDTO's name is not equal to the given one.
   */
  public S hasName(String name) {
    // check that actual UnsatisfiedReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting name of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualName = actual.name;
    if (!Objects.areEqual(actualName, name)) {
      failWithMessage(assertjErrorMessage, actual, name, actualName);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual UnsatisfiedReferenceDTO's target is equal to the given one.
   * @param target the given target to compare the actual UnsatisfiedReferenceDTO's target to.
   * @return this assertion object.
   * @throws AssertionError - if the actual UnsatisfiedReferenceDTO's target is not equal to the given one.
   */
  public S hasTarget(String target) {
    // check that actual UnsatisfiedReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting target of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualTarget = actual.target;
    if (!Objects.areEqual(actualTarget, target)) {
      failWithMessage(assertjErrorMessage, actual, target, actualTarget);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual UnsatisfiedReferenceDTO's targetServices contains the given org.osgi.framework.dto.ServiceReferenceDTO elements.
   * @param targetServices the given elements that should be contained in actual UnsatisfiedReferenceDTO's targetServices.
   * @return this assertion object.
   * @throws AssertionError if the actual UnsatisfiedReferenceDTO's targetServices does not contain all given org.osgi.framework.dto.ServiceReferenceDTO elements.
   */
  public S hasTargetServices(org.osgi.framework.dto.ServiceReferenceDTO... targetServices) {
    // check that actual UnsatisfiedReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // check that given org.osgi.framework.dto.ServiceReferenceDTO varargs is not null.
    if (targetServices == null) failWithMessage("Expecting targetServices parameter not to be null.");

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Assertions.assertThat(actual.targetServices).contains(targetServices);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual UnsatisfiedReferenceDTO's targetServices contains <b>only</b> the given org.osgi.framework.dto.ServiceReferenceDTO elements and nothing else in whatever order.
   *
   * @param targetServices the given elements that should be contained in actual UnsatisfiedReferenceDTO's targetServices.
   * @return this assertion object.
   * @throws AssertionError if the actual UnsatisfiedReferenceDTO's targetServices does not contain all given org.osgi.framework.dto.ServiceReferenceDTO elements and nothing else.
   */
  public S hasOnlyTargetServices(org.osgi.framework.dto.ServiceReferenceDTO... targetServices) {
    // check that actual UnsatisfiedReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // check that given org.osgi.framework.dto.ServiceReferenceDTO varargs is not null.
    if (targetServices == null) failWithMessage("Expecting targetServices parameter not to be null.");

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Assertions.assertThat(actual.targetServices).containsOnly(targetServices);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual UnsatisfiedReferenceDTO's targetServices does not contain the given org.osgi.framework.dto.ServiceReferenceDTO elements.
   *
   * @param targetServices the given elements that should not be in actual UnsatisfiedReferenceDTO's targetServices.
   * @return this assertion object.
   * @throws AssertionError if the actual UnsatisfiedReferenceDTO's targetServices contains any given org.osgi.framework.dto.ServiceReferenceDTO elements.
   */
  public S doesNotHaveTargetServices(org.osgi.framework.dto.ServiceReferenceDTO... targetServices) {
    // check that actual UnsatisfiedReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // check that given org.osgi.framework.dto.ServiceReferenceDTO varargs is not null.
    if (targetServices == null) failWithMessage("Expecting targetServices parameter not to be null.");

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Assertions.assertThat(actual.targetServices).doesNotContain(targetServices);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual UnsatisfiedReferenceDTO has no targetServices.
   * @return this assertion object.
   * @throws AssertionError if the actual UnsatisfiedReferenceDTO's targetServices is not empty.
   */
  public S hasNoTargetServices() {
    // check that actual UnsatisfiedReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting :\n  <%s>\nnot to have targetServices but had :\n  <%s>";

    // check that it is not empty
    if (actual.targetServices.length > 0)  {
      failWithMessage(assertjErrorMessage, actual, java.util.Arrays.toString(actual.targetServices));
    }

    // return the current assertion for method chaining
    return myself;
  }


}
