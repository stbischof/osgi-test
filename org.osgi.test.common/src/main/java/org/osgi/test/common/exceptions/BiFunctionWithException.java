/*
 * Copyright (c) OSGi Alliance (2019, 2021). All Rights Reserved.
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

package org.osgi.test.common.exceptions;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * BiFunction interface that allows exceptions.
 *
 * @param <T> the type 1 of the argument
 * @param <U> the type 2 of the argument
 * @param <R> the result type
 */
@FunctionalInterface
public interface BiFunctionWithException<T, U, R> {
	R apply(T t, U u) throws Exception;

	default BiFunction<T, U, R> orElseThrow() {
		return (t, u) -> {
			try {
				return apply(t, u);
			} catch (Exception e) {
				throw Exceptions.duck(e);
			}
		};
	}

	default BiFunction<T, U, R> orElse(R orElse) {
		return (t, u) -> {
			try {
				return apply(t, u);
			} catch (Exception e) {
				return orElse;
			}
		};
	}

	default BiFunction<T, U, R> orElseGet(Supplier<? extends R> orElseGet) {
		requireNonNull(orElseGet);
		return (t, u) -> {
			try {
				return apply(t, u);
			} catch (Exception e) {
				return orElseGet.get();
			}
		};
	}

	static <T, U, R> BiFunction<T, U, R> asBiFunction(BiFunctionWithException<T, U, R> unchecked) {
		return unchecked.orElseThrow();
	}

	static <T, U, R> BiFunction<T, U, R> asBiFunctionOrElse(BiFunctionWithException<T, U, R> unchecked, R orElse) {
		return unchecked.orElse(orElse);
	}

	static <T, U, R> BiFunction<T, U, R> asBiFunctionOrElseGet(BiFunctionWithException<T, U, R> unchecked,
		Supplier<? extends R> orElseGet) {
		return unchecked.orElseGet(orElseGet);
	}
}
