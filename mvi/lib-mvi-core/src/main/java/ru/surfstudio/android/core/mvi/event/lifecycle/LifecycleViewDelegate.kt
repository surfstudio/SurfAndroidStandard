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
package ru.surfstudio.android.core.mvi.event.lifecycle

import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyDelegate

/**
 * Делегат всех событий жизненного цикла для View.
 */
interface LifecycleViewDelegate :
        OnViewReadyDelegate,
        OnStartDelegate,
        OnResumeDelegate,
        OnPauseDelegate,
        OnStopDelegate,
        OnViewDestroyDelegate,
        OnCompletelyDestroyDelegate,
        OnSaveStateDelegate,
        OnRestoreStateDelegate