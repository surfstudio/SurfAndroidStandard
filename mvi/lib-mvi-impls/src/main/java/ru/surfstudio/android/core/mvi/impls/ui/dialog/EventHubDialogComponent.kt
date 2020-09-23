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
package ru.surfstudio.android.core.mvi.impls.ui.dialog

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub

/**
 * Component for simple dialogs, which live in parent dagger scope.
 *
 * You need to inherit your parent's ScreenComponent from this component to
 * make injection of [ScreenEventHub] to a dialog possible.
 */
interface EventHubDialogComponent<E : Event> {

    fun screenHub(): ScreenEventHub<E>
}
