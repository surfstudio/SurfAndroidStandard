package ru.surfstudio.android.core.ui.sample.ui.core

import android.os.Bundle
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.logger.Logger

class CustomOnSaveStateDelegate(eventDelegateManager: ScreenEventDelegateManager
) : OnSaveStateDelegate {

    init {
        eventDelegateManager.registerDelegate(this)
    }

    override fun onSaveState(outState: Bundle?) {
        Logger.d("Custom OnSaveStateDelegate onSaveState")
    }
}