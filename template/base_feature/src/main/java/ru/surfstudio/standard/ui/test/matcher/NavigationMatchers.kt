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
package ru.surfstudio.standard.ui.test.matcher

import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldBeTypeOf
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent

/**
 * Verifies that [NavCommandsEvent] contains navigation command of type [T].
 */
inline fun <reified T : NavigationCommand> NavCommandsEvent?.shouldBeNavigationCommand(): T {
    return this?.commands?.firstOrNull().shouldBeTypeOf<T>()
}

/**
 * Verifies that [NavigationCommand] contains [Route] of type [T].
 */
inline fun <reified T : Route> NavigationCommand?.withRoute(): T {
    return this?.route.shouldBeTypeOf<T>()
}

/**
 * Verifies that [NavigationCommand] contains exact [route] of type [T].
 */
inline fun <reified T : Route> NavigationCommand?.withRoute(route: T) {
    return this?.route.shouldBeSameInstanceAs(route)
}