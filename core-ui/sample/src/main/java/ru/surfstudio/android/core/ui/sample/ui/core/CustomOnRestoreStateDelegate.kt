package ru.surfstudio.android.core.ui.sample.ui.core

import android.os.Bundle
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.logger.Logger

class CustomOnRestoreStateDelegate(eventDelegateManager: ScreenEventDelegateManager
) : OnRestoreStateDelegate {

    init {
        eventDelegateManager.registerDelegate(this)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        Logger.d("Custom OnRestoreStateDelegate onRestoreState")
    }
}