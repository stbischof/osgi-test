package org.osgi.test.assertj.dto.service.http.runtime.dto;

import org.osgi.service.http.runtime.dto.FailedErrorPageDTO;

/**
 * Abstract base class for {@link FailedErrorPageDTO} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractFailedErrorPageDTOAssert<S extends AbstractFailedErrorPageDTOAssert<S, A>, A extends FailedErrorPageDTO> extends AbstractErrorPageDTOAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractFailedErrorPageDTOAssert}</code> to make assertions on actual FailedErrorPageDTO.
   * @param actual the FailedErrorPageDTO we want to make assertions on.
   */
  protected AbstractFailedErrorPageDTOAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual FailedErrorPageDTO's failureReason is equal to the given one.
   * @param failureReason the given failureReason to compare the actual FailedErrorPageDTO's failureReason to.
   * @return this assertion object.
   * @throws AssertionError - if the actual FailedErrorPageDTO's failureReason is not equal to the given one.
   */
  public S hasFailureReason(int failureReason) {
    // check that actual FailedErrorPageDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting failureReason of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // check
    int actualFailureReason = actual.failureReason;
    if (actualFailureReason != failureReason) {
      failWithMessage(assertjErrorMessage, actual, failureReason, actualFailureReason);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
