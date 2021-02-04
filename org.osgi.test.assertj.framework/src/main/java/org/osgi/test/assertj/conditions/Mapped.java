package org.osgi.test.assertj.conditions;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.assertj.core.api.Condition;

public class Mapped<FROM, T> extends Condition<FROM> {


  private static <FROM, TO> BiFunction<FROM, TO, String> mappingDescription(String using) {

    return (from, to) -> {

      StringBuilder sb = new StringBuilder();
      sb.append("mapped");
      if (using != null) {
        sb.append(String.format("%n   using: %s", using));
      }
		sb.append(String.format("%n   from: <%s> %s%n", from == null ? "[null]"
			: from.getClass()
				.getSimpleName(),
			from));
		sb.append(String.format("   to:   <%s> %s%n", to == null ? "[null]"
			: to.getClass()
				.getSimpleName(),
			to));
      sb.append("   then checked:");
      return sb.toString();
    };
  }

	public static <FROM, TO> Mapped<FROM, TO> mapped(Function<FROM, TO> mapping, String mappingDescription,
		Condition<TO> condition) {

		return new Mapped<>(mapping, mappingDescription, condition);
	}

	public static <FROM, TO> Mapped<FROM, TO> mapped(Function<FROM, TO> mapping, Condition<TO> condition) {

		return new Mapped<>(mapping, null, condition);
	}

  private Condition<T> condition;

  private Function<FROM, T> mapping;

  private String mappingDescription;

  private Mapped(Function<FROM, T> mapping, String mappingDescription, Condition<T> condition) {

    super("");
    this.mapping = mapping;
    this.mappingDescription = mappingDescription;
    this.condition = condition;
    calcDescribedAs("mapping");
  }

  protected void calcDescribedAs(String mappingDescription) {

    describedAs(mappingDescription + " [\n" + "      %-10s\n" + "]", condition);
  }

  @Override
  public boolean matches(FROM value) {

    T mappedObject = mapping.apply(value);
    boolean matchResult = condition.matches(mappedObject);
    String desc = mappingDescription(mappingDescription).apply(value, mappedObject);
    calcDescribedAs(desc);
    return matchResult;
  }
}