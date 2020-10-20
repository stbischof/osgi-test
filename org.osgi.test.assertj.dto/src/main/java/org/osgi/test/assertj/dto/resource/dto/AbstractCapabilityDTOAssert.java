package org.osgi.test.assertj.dto.resource.dto;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.util.Objects;
import org.osgi.resource.dto.CapabilityDTO;

/**
 * Abstract base class for {@link CapabilityDTO} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractCapabilityDTOAssert<S extends AbstractCapabilityDTOAssert<S, A>, A extends CapabilityDTO> extends AbstractObjectAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractCapabilityDTOAssert}</code> to make assertions on actual CapabilityDTO.
   * @param actual the CapabilityDTO we want to make assertions on.
   */
  protected AbstractCapabilityDTOAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual CapabilityDTO's attributes is equal to the given one.
   * @param attributes the given attributes to compare the actual CapabilityDTO's attributes to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CapabilityDTO's attributes is not equal to the given one.
   */
  public S hasAttributes(java.util.Map attributes) {
    // check that actual CapabilityDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting attributes of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    java.util.Map actualAttributes = actual.attributes;
    if (!Objects.areEqual(actualAttributes, attributes)) {
      failWithMessage(assertjErrorMessage, actual, attributes, actualAttributes);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual CapabilityDTO's directives is equal to the given one.
   * @param directives the given directives to compare the actual CapabilityDTO's directives to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CapabilityDTO's directives is not equal to the given one.
   */
  public S hasDirectives(java.util.Map directives) {
    // check that actual CapabilityDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting directives of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    java.util.Map actualDirectives = actual.directives;
    if (!Objects.areEqual(actualDirectives, directives)) {
      failWithMessage(assertjErrorMessage, actual, directives, actualDirectives);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual CapabilityDTO's id is equal to the given one.
   * @param id the given id to compare the actual CapabilityDTO's id to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CapabilityDTO's id is not equal to the given one.
   */
  public S hasId(int id) {
    // check that actual CapabilityDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting id of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // check
    int actualId = actual.id;
    if (actualId != id) {
      failWithMessage(assertjErrorMessage, actual, id, actualId);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual CapabilityDTO's namespace is equal to the given one.
   * @param namespace the given namespace to compare the actual CapabilityDTO's namespace to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CapabilityDTO's namespace is not equal to the given one.
   */
  public S hasNamespace(String namespace) {
    // check that actual CapabilityDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting namespace of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualNamespace = actual.namespace;
    if (!Objects.areEqual(actualNamespace, namespace)) {
      failWithMessage(assertjErrorMessage, actual, namespace, actualNamespace);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual CapabilityDTO's resource is equal to the given one.
   * @param resource the given resource to compare the actual CapabilityDTO's resource to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CapabilityDTO's resource is not equal to the given one.
   */
  public S hasResource(int resource) {
    // check that actual CapabilityDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting resource of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // check
    int actualResource = actual.resource;
    if (actualResource != resource) {
      failWithMessage(assertjErrorMessage, actual, resource, actualResource);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
