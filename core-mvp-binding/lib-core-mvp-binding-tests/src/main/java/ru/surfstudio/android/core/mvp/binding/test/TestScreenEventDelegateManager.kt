package ru.surfstudio.android.core.mvp.binding.test

import ru.surfstudio.android.core.ui.ScreenType
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.base.ScreenEvent
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate

/**
 * Тестовая реализация [ScreenEventDelegateManager]
 */
class TestScreenEventDelegateManager : ScreenEventDelegateManager {

    override fun registerDelegate(delegate: ScreenEventDelegate?) {
        /* do nothing */
    }

    override fun registerDelegate(delegate: ScreenEventDelegate?, emitterType: ScreenType?) {
        /* do nothing */
    }

    override fun registerDelegate(delegate: ScreenEventDelegate?, emitterType: ScreenType?, eventType: Class<out ScreenEvent>?) {
        /* do nothing */
    }

    override fun <E : ScreenEvent?> unregisterDelegate(delegate: ScreenEventDelegate?, event: Class<E>?): Boolean {
        /* do nothing */
        return true
    }

    override fun unregisterDelegate(delegate: ScreenEventDelegate?): Boolean {
        /* do nothing */
        return true
    }

    override fun <E : ScreenEvent?, D : ScreenEventDelegate?, R : Any?> sendEvent(event: E): R? {
        /* do nothing */
        return null
    }

    override fun destroy() {
        /* do nothing */
    }

    override fun sendUnhandledEvents() {
        /* do nothing */
    }
}