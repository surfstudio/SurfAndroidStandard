package ru.surfstudio.android.core.mvp.sample.ui.core

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeDelegate
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import javax.inject.Inject

@PerScreen
class CustomOnResumeDelegate @Inject constructor(eventDelegateManager: ScreenEventDelegateManager
) : OnResumeDelegate {

    init {
        eventDelegateManager.registerDelegate(this)
    }

    override fun onResume() = Logger.d("CustomOnResumeDelegate onResume")
}