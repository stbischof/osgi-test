package org.osgi.test.assertj.dto.resource.dto;

import org.osgi.resource.dto.CapabilityDTO;

/**
 * {@link CapabilityDTO} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it, 
 * extend {@link AbstractCapabilityDTOAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class CapabilityDTOAssert extends AbstractCapabilityDTOAssert<CapabilityDTOAssert, CapabilityDTO> {

  /**
   * Creates a new <code>{@link CapabilityDTOAssert}</code> to make assertions on actual CapabilityDTO.
   * @param actual the CapabilityDTO we want to make assertions on.
   */
  public CapabilityDTOAssert(CapabilityDTO actual) {
    super(actual, CapabilityDTOAssert.class);
  }

  /**
   * An entry point for CapabilityDTOAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myCapabilityDTO)</code> and get specific assertion with code completion.
   * @param actual the CapabilityDTO we want to make assertions on.
   * @return a new <code>{@link CapabilityDTOAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static CapabilityDTOAssert assertThat(CapabilityDTO actual) {
    return new CapabilityDTOAssert(actual);
  }
}
