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
package ru.surfstudio.android.core.mvi.impls.ui.freezer

import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyDelegate

/**
 * Subscription freezer, that works with android lifecycle events:
 * freezes subscription on pause or view destroy, and resumes it on resume.
 */
class LifecycleSubscriptionFreezer(
        screenEventDelegateManager: ScreenEventDelegateManager
) :
        OnResumeDelegate,
        OnPauseDelegate,
        OnViewDestroyDelegate, SubscriptionFreezer() {

    override val freezeSelector: BehaviorSubject<Boolean> = BehaviorSubject.create()
    var freezeEventsOnPause: Boolean = true

    init {
        screenEventDelegateManager.registerDelegate(this)
    }

    override fun onResume() {
        freezeSelector.onNext(false)
    }

    override fun onPause() {
        if (freezeEventsOnPause) {
            freezeSelector.onNext(true)
        }
    }

    override fun onViewDestroy() {
        freezeSelector.onNext(true)
    }
}