package ru.surfstudio.android.core.mvp.binding.react.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView

abstract class BaseReactActivityView : BaseRxActivityView(), BaseReactView {

    abstract override val hub: RxEventHub

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        getFeatures().forEach { hub.observeEvents() bindTo it::react }
    }

}

abstract class BaseReactFragmentView : BaseRxFragmentView(), BaseReactView {

    abstract override val hub: RxEventHub

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        getFeatures().forEach { hub.observeEvents() bindTo it::react }
    }
}