package org.osgi.test.assertj.dto.service.component.runtime.dto;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.util.Objects;
import org.osgi.service.component.runtime.dto.ReferenceDTO;

/**
 * Abstract base class for {@link ReferenceDTO} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractReferenceDTOAssert<S extends AbstractReferenceDTOAssert<S, A>, A extends ReferenceDTO> extends AbstractObjectAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractReferenceDTOAssert}</code> to make assertions on actual ReferenceDTO.
   * @param actual the ReferenceDTO we want to make assertions on.
   */
  protected AbstractReferenceDTOAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual ReferenceDTO's bind is equal to the given one.
   * @param bind the given bind to compare the actual ReferenceDTO's bind to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's bind is not equal to the given one.
   */
  public S hasBind(String bind) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting bind of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualBind = actual.bind;
    if (!Objects.areEqual(actualBind, bind)) {
      failWithMessage(assertjErrorMessage, actual, bind, actualBind);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's cardinality is equal to the given one.
   * @param cardinality the given cardinality to compare the actual ReferenceDTO's cardinality to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's cardinality is not equal to the given one.
   */
  public S hasCardinality(String cardinality) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting cardinality of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualCardinality = actual.cardinality;
    if (!Objects.areEqual(actualCardinality, cardinality)) {
      failWithMessage(assertjErrorMessage, actual, cardinality, actualCardinality);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's field is equal to the given one.
   * @param field the given field to compare the actual ReferenceDTO's field to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's field is not equal to the given one.
   */
  public S hasField(String field) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting field of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualField = actual.field;
    if (!Objects.areEqual(actualField, field)) {
      failWithMessage(assertjErrorMessage, actual, field, actualField);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's fieldOption is equal to the given one.
   * @param fieldOption the given fieldOption to compare the actual ReferenceDTO's fieldOption to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's fieldOption is not equal to the given one.
   */
  public S hasFieldOption(String fieldOption) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting fieldOption of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualFieldOption = actual.fieldOption;
    if (!Objects.areEqual(actualFieldOption, fieldOption)) {
      failWithMessage(assertjErrorMessage, actual, fieldOption, actualFieldOption);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's interfaceName is equal to the given one.
   * @param interfaceName the given interfaceName to compare the actual ReferenceDTO's interfaceName to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's interfaceName is not equal to the given one.
   */
  public S hasInterfaceName(String interfaceName) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting interfaceName of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualInterfaceName = actual.interfaceName;
    if (!Objects.areEqual(actualInterfaceName, interfaceName)) {
      failWithMessage(assertjErrorMessage, actual, interfaceName, actualInterfaceName);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's name is equal to the given one.
   * @param name the given name to compare the actual ReferenceDTO's name to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's name is not equal to the given one.
   */
  public S hasName(String name) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
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
   * Verifies that the actual ReferenceDTO's policy is equal to the given one.
   * @param policy the given policy to compare the actual ReferenceDTO's policy to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's policy is not equal to the given one.
   */
  public S hasPolicy(String policy) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting policy of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualPolicy = actual.policy;
    if (!Objects.areEqual(actualPolicy, policy)) {
      failWithMessage(assertjErrorMessage, actual, policy, actualPolicy);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's policyOption is equal to the given one.
   * @param policyOption the given policyOption to compare the actual ReferenceDTO's policyOption to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's policyOption is not equal to the given one.
   */
  public S hasPolicyOption(String policyOption) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting policyOption of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualPolicyOption = actual.policyOption;
    if (!Objects.areEqual(actualPolicyOption, policyOption)) {
      failWithMessage(assertjErrorMessage, actual, policyOption, actualPolicyOption);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's scope is equal to the given one.
   * @param scope the given scope to compare the actual ReferenceDTO's scope to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's scope is not equal to the given one.
   */
  public S hasScope(String scope) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting scope of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualScope = actual.scope;
    if (!Objects.areEqual(actualScope, scope)) {
      failWithMessage(assertjErrorMessage, actual, scope, actualScope);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's target is equal to the given one.
   * @param target the given target to compare the actual ReferenceDTO's target to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's target is not equal to the given one.
   */
  public S hasTarget(String target) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
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
   * Verifies that the actual ReferenceDTO's unbind is equal to the given one.
   * @param unbind the given unbind to compare the actual ReferenceDTO's unbind to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's unbind is not equal to the given one.
   */
  public S hasUnbind(String unbind) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting unbind of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualUnbind = actual.unbind;
    if (!Objects.areEqual(actualUnbind, unbind)) {
      failWithMessage(assertjErrorMessage, actual, unbind, actualUnbind);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual ReferenceDTO's updated is equal to the given one.
   * @param updated the given updated to compare the actual ReferenceDTO's updated to.
   * @return this assertion object.
   * @throws AssertionError - if the actual ReferenceDTO's updated is not equal to the given one.
   */
  public S hasUpdated(String updated) {
    // check that actual ReferenceDTO we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting updated of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualUpdated = actual.updated;
    if (!Objects.areEqual(actualUpdated, updated)) {
      failWithMessage(assertjErrorMessage, actual, updated, actualUpdated);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
