/*
  Copyright (c) 2018-present, SurfStudio LLC, Georgiy Kartashov.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.standard.base.test.util

import io.kotest.data.*
import io.kotest.mpp.reflection

/**
 * Extension method for convinient implementation of a parametric tests with one parameter.
 *
 * @param values values of type [A].
 * @param testfn test body.
 */
fun <A> forAll(vararg values: A, testfn: (A) -> Unit) {
    val params = reflection.paramNames(testfn) ?: emptyList<String>()
    val paramA = params.getOrElse(0) { "a" }
    table(headers(paramA), *values.map(::Row1).toTypedArray()).forAll { a -> testfn(a) }
}

/**
 * Extension method for convinient implementation of a parametric tests with two ([Pair]) parameterers.
 *
 * @param values [Pair]s of [A] and [B] types.
 * @param testfn test body.
 */
fun <A, B> forAll(vararg valuesPairs: Pair<A, B>, testfn: (A, B) -> Unit) {
    val params = reflection.paramNames(testfn) ?: emptyList<String>()
    val paramA = params.getOrElse(0) { "a" }
    val paramB = params.getOrElse(1) { "b" }
    table(
            headers(paramA, paramB),
            *valuesPairs.map { Row2(it.first, it.second) }.toTypedArray()
    ).forAll { a, b -> testfn(a, b) }
}
