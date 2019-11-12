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