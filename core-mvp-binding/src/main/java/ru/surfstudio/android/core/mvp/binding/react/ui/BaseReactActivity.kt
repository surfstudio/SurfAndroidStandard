package ru.surfstudio.android.core.mvp.binding.react.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView

abstract class BaseReactActivity : BaseRxActivityView(), BaseReactView {

    abstract override val hub: RxEventHub

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        getFeatures().forEach { hub.observeEvents() bindTo it::react }
    }

}