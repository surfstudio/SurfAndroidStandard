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
package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent

/**
 * Base close screen event.
 * Doesn't close any screen by itself,
 * to close specific screen, you need to it one of it's accessors.
 * @see [CloseActivityEvent]
 * @see [CloseTaskEvent]
 * @see [CloseWithResultEvent]
 * @see [CloseDialogEvent]
 * @see [CloseFragmentEvent]
 */
interface CloseScreenEvent : NavigationEvent