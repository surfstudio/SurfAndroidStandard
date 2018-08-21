package ru.surfstudio.android.core.mvp.sample.ui.core

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.lifecycle.destroy.OnDestroyDelegate
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import javax.inject.Inject

@PerScreen
class CustomOnDestroyDelegate @Inject constructor(eventDelegateManager: ScreenEventDelegateManager
) : OnDestroyDelegate {

    init {
        eventDelegateManager.registerDelegate(this)
    }

    override fun onDestroy() = Logger.d("CustomOnDestroyDelegate onDestroy")
}