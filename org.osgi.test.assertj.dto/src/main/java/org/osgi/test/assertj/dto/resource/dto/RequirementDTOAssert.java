package org.osgi.test.assertj.dto.resource.dto;

import org.osgi.resource.dto.RequirementDTO;

/**
 * {@link RequirementDTO} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it, 
 * extend {@link AbstractRequirementDTOAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class RequirementDTOAssert extends AbstractRequirementDTOAssert<RequirementDTOAssert, RequirementDTO> {

  /**
   * Creates a new <code>{@link RequirementDTOAssert}</code> to make assertions on actual RequirementDTO.
   * @param actual the RequirementDTO we want to make assertions on.
   */
  public RequirementDTOAssert(RequirementDTO actual) {
    super(actual, RequirementDTOAssert.class);
  }

  /**
   * An entry point for RequirementDTOAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myRequirementDTO)</code> and get specific assertion with code completion.
   * @param actual the RequirementDTO we want to make assertions on.
   * @return a new <code>{@link RequirementDTOAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static RequirementDTOAssert assertThat(RequirementDTO actual) {
    return new RequirementDTOAssert(actual);
  }
}
