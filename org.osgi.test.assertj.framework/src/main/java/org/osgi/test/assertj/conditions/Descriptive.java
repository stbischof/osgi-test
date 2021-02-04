package org.osgi.test.assertj.conditions;

import java.util.function.BiPredicate;
import java.util.function.Function;

import org.assertj.core.api.Condition;

/**
 * Descriptive that shows the expected and the tested value in the
 * description.
 */
public final class Descriptive<T, EXPECTED> extends Condition<T> {

  private EXPECTED expected;

  private BiPredicate<T, EXPECTED> check;

  private String checkDescription;

  private Function<T, ?> transformGiven;

  private Function<EXPECTED, ?> transformExpected;


  private enum State {
    NOT_EXECUTED, OK, FAILED
  }

  public static Function<State, String> defaultState() {

    return state -> {
      switch (state) {
        case NOT_EXECUTED:
          return "[NOT EXECUTED]";
        case OK:
          return "[OK] ";
        case FAILED:
          return "[FAILED] ";
        default:
          return "[UNKNOWN] ";
      }
    };

  }

  public static <T,EXPECTED> Descriptive<T,EXPECTED> descriptive(EXPECTED expected, BiPredicate<T, EXPECTED> check) {
	  return new Descriptive<T, EXPECTED>(expected,check,"",null,null);
  }

  public static <T,EXPECTED> Descriptive<T,EXPECTED> descriptive(EXPECTED expected, BiPredicate<T, EXPECTED> check,
	      String checkDescription) {
	  return new Descriptive<T, EXPECTED>(expected,check,checkDescription,null,null);
  }
  public static <T,EXPECTED> Descriptive<T,EXPECTED> descriptive(EXPECTED expected, BiPredicate<T, EXPECTED> check,
	      String checkDescription, Function<EXPECTED, ?> transformExpected,
	      Function<T, ?> transformGiven) {
	  return new Descriptive<T, EXPECTED>(expected,check,checkDescription,transformExpected,transformGiven);
  }

  private Descriptive(EXPECTED expected, BiPredicate<T, EXPECTED> check,
      String checkDescription, Function<EXPECTED, ?> transformExpected,
      Function<T, ?> transformGiven) {

    this.expected = expected;
    this.check = check;
    this.checkDescription = checkDescription;
    this.transformExpected = transformExpected;
    this.transformGiven = transformGiven;
    describedAs("%s%s %s", defaultState().apply(State.NOT_EXECUTED), checkDescription,
        transformIf(transformExpected, expected));
  }

  @Override
  public boolean matches(T value) {

    boolean match = check.test(value, expected);
    if (match) {
      describedAs("%s%s <%s>", defaultState().apply(State.OK), checkDescription,
          transformIf(transformExpected, expected));
    } else {
      describedAs("%s%s <%s> but was <%s>", defaultState().apply(State.FAILED), checkDescription,
          transformIf(transformExpected, expected), transformIf(transformGiven, value));
    }
    return match;
  }

  private static <E> Object transformIf(Function<E, ?> transform, E object) {

    return transform == null ? object : transform.apply(object);
  }
}
