/*
 * Copyright (c) OSGi Alliance (2019). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgi.test.common.filter;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.function.Function;

import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.test.common.exceptions.FunctionWithException;

public class Filters {

	private Filters() {}

	private static final Function<String, Filter> createFilter = FunctionWithException
		.asFunction(FrameworkUtil::createFilter);

	/**
	 * Utility method for creating a {@link Filter} using a format string.
	 * <p>
	 * See {@link String#format(String, Object...)}
	 *
	 * @param format a format string
	 * @param args format arguments
	 * @return filter
	 */
	public static Filter format(String format, Object... args) {
		String filter = String.format(format, args);
		return createFilter.apply(filter);
	}

	/**
	 * Creates a filter String looking for all properties in the
	 * {@link Dictionary} given.
	 *
	 * @param properties the {@link Dictionary} with the service properties.
	 * @return the {@link Filter} Object
	 * @throws IllegalArgumentException if {@link Dictionary} is
	 *             <code>null</code> or empty or {@link Filter} could not be
	 *             created.
	 */
	public static Filter createFilter(Dictionary<String, Object> properties) {

		if (properties == null || properties.isEmpty()) {
			throw new IllegalArgumentException("Provided configuration properties are empty. Cannot create a filter");
		}

		StringBuilder sb = new StringBuilder("(&");
		Enumeration<String> keys = properties.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object value = properties.get(key);
			String valueString = value.toString();
			valueString = valueString.replace("(", "\\(")
				.replace(")", "\\)");
			sb.append(String.format("(%s=%s)", key, valueString));
		}
		sb.append(")");
		try {
			return FrameworkUtil.createFilter(sb.toString());
		} catch (InvalidSyntaxException e) {
			throw new IllegalArgumentException("Could not create filter " + e.getFilter(), e);
		}
	}


}

