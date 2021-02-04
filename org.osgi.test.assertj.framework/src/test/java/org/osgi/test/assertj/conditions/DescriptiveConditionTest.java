package org.osgi.test.assertj.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

public class DescriptiveConditionTest {

  /**
   *
   * <pre>
   * mapped
     using: StringBuilder::toString magic
     from: <StringBuilder> foooo
     to:   <String> foooo
     then checked: [
        all of:[
     [NOT EXECUTED]shorter than 100,
     [NOT EXECUTED]not be longer 4 (max size),
     any of:[
        [NOT EXECUTED]shorter than 100,
        [NOT EXECUTED]not be longer 4 (max size),
        all of:[
           [NOT EXECUTED]shorter than 100,
           [NOT EXECUTED]not be longer 4 (max size)
        ]
     ],
     [NOT EXECUTED]shorter than 100
  ]
  ]
  [OK] shorter than <100>
  [FAILED] not be longer <4 (max size)> but was <5 (original word: foooo)>
   * </pre>
   */
  @Test
  public static void Constructor_test() {

    Condition<String> dynamic1 = Descriptive.descriptive(100,(String given, Integer expected) -> given.length() < expected,"shorter than");


    assertThat(dynamic1.toString()).matches("[NOT EXECUTED] shorter than <100>");

    assertThat(dynamic1.matches("foooo")).isTrue();
    System.out.println(dynamic1);
    assertThat(dynamic1.toString()).matches("[OK] shorter than <100>");

    Condition<String> dynamic2 = Descriptive.descriptive(4,
    		(String given,Integer expected) -> given.length() < expected,
            "not be longer",
            (i) -> i + " (max size)",
            (s) -> String.format("%s (original word: %s)", s.length(), s));

    assertThat(dynamic2.matches("foooo")).isTrue();
    System.out.println(dynamic2);
    assertThat(dynamic2.toString())
        .matches("[FAILED] not be longer <4 (max size)> but was <5 (original word: foooo)>");

  }
}
