package org.osgi.test.assertj.dto.service.component.runtime.dto;

import org.osgi.service.component.runtime.dto.UnsatisfiedReferenceDTO;

/**
 * {@link UnsatisfiedReferenceDTO} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it, 
 * extend {@link AbstractUnsatisfiedReferenceDTOAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class UnsatisfiedReferenceDTOAssert extends AbstractUnsatisfiedReferenceDTOAssert<UnsatisfiedReferenceDTOAssert, UnsatisfiedReferenceDTO> {

  /**
   * Creates a new <code>{@link UnsatisfiedReferenceDTOAssert}</code> to make assertions on actual UnsatisfiedReferenceDTO.
   * @param actual the UnsatisfiedReferenceDTO we want to make assertions on.
   */
  public UnsatisfiedReferenceDTOAssert(UnsatisfiedReferenceDTO actual) {
    super(actual, UnsatisfiedReferenceDTOAssert.class);
  }

  /**
   * An entry point for UnsatisfiedReferenceDTOAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myUnsatisfiedReferenceDTO)</code> and get specific assertion with code completion.
   * @param actual the UnsatisfiedReferenceDTO we want to make assertions on.
   * @return a new <code>{@link UnsatisfiedReferenceDTOAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static UnsatisfiedReferenceDTOAssert assertThat(UnsatisfiedReferenceDTO actual) {
    return new UnsatisfiedReferenceDTOAssert(actual);
  }
}
