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
package ru.surfstudio.android.core.mvi.impls.event.hub.back_pressed

import ru.surfstudio.android.core.mvi.event.factory.ParamlessEventFactory
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.EventHub
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate

/**
 * [EventHub], that handles BackPressedEvents (clicks on system "Back" button)
 */
interface BackPressedEventHub<E : Event, EventStream> : EventHub<E, EventStream>, OnBackPressedDelegate {

    val backPressedCreator: ParamlessEventFactory<E>?

    override fun onBackPressed(): Boolean {
        val event = backPressedCreator?.invoke() ?: return false
        emit(event)
        return true
    }
}