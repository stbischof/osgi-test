package org.osgi.test.assertj.dto.service.component.runtime.dto;

import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

/**
 * {@link ComponentDescriptionDTO} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it, 
 * extend {@link AbstractComponentDescriptionDTOAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class ComponentDescriptionDTOAssert extends AbstractComponentDescriptionDTOAssert<ComponentDescriptionDTOAssert, ComponentDescriptionDTO> {

  /**
   * Creates a new <code>{@link ComponentDescriptionDTOAssert}</code> to make assertions on actual ComponentDescriptionDTO.
   * @param actual the ComponentDescriptionDTO we want to make assertions on.
   */
  public ComponentDescriptionDTOAssert(ComponentDescriptionDTO actual) {
    super(actual, ComponentDescriptionDTOAssert.class);
  }

  /**
   * An entry point for ComponentDescriptionDTOAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myComponentDescriptionDTO)</code> and get specific assertion with code completion.
   * @param actual the ComponentDescriptionDTO we want to make assertions on.
   * @return a new <code>{@link ComponentDescriptionDTOAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static ComponentDescriptionDTOAssert assertThat(ComponentDescriptionDTO actual) {
    return new ComponentDescriptionDTOAssert(actual);
  }
}
