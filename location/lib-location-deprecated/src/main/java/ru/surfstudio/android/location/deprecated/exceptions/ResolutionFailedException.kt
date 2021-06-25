/*
 * Copyright 2016 Valeriy Shtaits.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.location.deprecated.exceptions

/**
 * Исключение, возникающее, если попытка решения проблемы с получением местоположения не удалась.
 *
 * @param resolvingThrowable исключение, которое требовалось решить.
 * @param failingThrowable исключение, которое возникло в ходе решения.
 */
class ResolutionFailedException(
        val resolvingThrowable: Throwable?,
        val failingThrowable: Throwable?
) : RuntimeException("Resolving $resolvingThrowable failed with $failingThrowable")