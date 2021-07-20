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

import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close.*
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.open.OpenScreenEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.open.OpenScreenEvent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable


/**
 * Opens next screen
 *
 * @param route screen route
 */
data class OpenScreen(override val route: Route) : OpenScreenEvent

/**
 * Opens next screen for result
 *
 * @param route screen route
 */
data class OpenScreenForResult<R : Serializable>(
        override val route: SupportOnActivityResultRoute<R>
) : OpenScreenEvent

/**
 * Closes current Activity
 */
class CloseActivity : CloseActivityEvent {
    override fun toString(): String = "CloseActivity"
}

/**
 * Closes activity taskAffinity
 */
class CloseTask : CloseTaskEvent {
    override fun toString(): String = "CloseTask"
}

/**
 * Closes screen with result
 *
 * @param route screen route
 * @param result screen result
 * @param isSuccess is result successful
 */
data class CloseWithResult<R : Serializable>(
        override val route: SupportOnActivityResultRoute<R>,
        override val result: R,
        override val isSuccess: Boolean
) : CloseWithResultEvent<R>

/**
 * Closes dialog
 */
data class CloseDialog(override val route: DialogRoute) : CloseDialogEvent

/**
 * Closes fragment
 */
data class CloseFragment(override val route: FragmentRoute) : CloseFragmentEvent

/**
 * Observes screen result event
 *
 * @param routeClass class to be observed
 * @param result screen result
 */
data class ObserveResult<R : Serializable>(
        override val routeClass: Class<out SupportOnActivityResultRoute<R>>,
        override var result: ScreenResult<R>? = null
) : ObserveResultEvent<R>