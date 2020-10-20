package org.osgi.test.assertj.dto.service.http.runtime.dto;

import org.osgi.service.http.runtime.dto.ListenerDTO;

/**
 * {@link ListenerDTO} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it, 
 * extend {@link AbstractListenerDTOAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class ListenerDTOAssert extends AbstractListenerDTOAssert<ListenerDTOAssert, ListenerDTO> {

  /**
   * Creates a new <code>{@link ListenerDTOAssert}</code> to make assertions on actual ListenerDTO.
   * @param actual the ListenerDTO we want to make assertions on.
   */
  public ListenerDTOAssert(ListenerDTO actual) {
    super(actual, ListenerDTOAssert.class);
  }

  /**
   * An entry point for ListenerDTOAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myListenerDTO)</code> and get specific assertion with code completion.
   * @param actual the ListenerDTO we want to make assertions on.
   * @return a new <code>{@link ListenerDTOAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static ListenerDTOAssert assertThat(ListenerDTO actual) {
    return new ListenerDTOAssert(actual);
  }
}
