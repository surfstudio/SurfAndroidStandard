/*
  Copyright (c) 2020, SurfStudio LLC.

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
package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

/**
 * Opens next screen
 *
 * @param route screen route
 */
fun <T : NavigationComposition> T.open(route: Route): T =
        this.apply { events = listOf(OpenScreen(route)) }

/**
 * Opens next screen for result
 *
 * @param route screen route
 */
fun <T : NavigationComposition, R : Serializable> T.openForResult(
        route: SupportOnActivityResultRoute<R>
): T = this.apply { events = listOf(OpenScreenForResult(route)) }

/**
 * Closes current activity
 */
fun <T : NavigationComposition> T.close(): T =
        this.apply { events = listOf(CloseActivity()) }

/**
 * Closes activity taskAffinity
 */
fun <T : NavigationComposition> T.closeTask(): T =
        this.apply { events = listOf(CloseTask()) }

/**
 * Closes screen with result
 *
 * @param route screen route
 * @param result screen result
 * @param isSuccess is result successful
 */
fun <T : NavigationComposition, R : Serializable> T.close(
        route: SupportOnActivityResultRoute<R>,
        result: R,
        isSuccess: Boolean = true
): T = this.apply { events = listOf(CloseWithResult(route, result, isSuccess)) }

/**
 * Closes dialog
 */
fun <T : NavigationComposition> T.close(route: DialogRoute) =
        this.apply { events = listOf(CloseDialog(route)) }

/**
 * Closes fragment
 */
fun <T : NavigationComposition> T.close(route: FragmentRoute) =
        this.apply { events = listOf(CloseFragment(route)) }

/**
 * Observes screen result event
 *
 * @param routeClass class to be observed
 */
fun <T : NavigationComposition, R : Serializable> T.observeResult(
        routeClass: Class<out SupportOnActivityResultRoute<R>>
): T = this.apply { events = listOf(ObserveResult(routeClass)) }