package ru.surfstudio.android.mvp.widget.event.delegate

import ru.surfstudio.android.core.ui.ScreenType
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.base.ScreenEvent
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate

class WidgetScreenEventDelegateManager : ScreenEventDelegateManager{

    override fun registerDelegate(delegate: ScreenEventDelegate?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerDelegate(delegate: ScreenEventDelegate?, emitterType: ScreenType?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterDelegate(delegate: ScreenEventDelegate?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <E : ScreenEvent?, D : ScreenEventDelegate?, R : Any?> sendEvent(event: E): R {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}