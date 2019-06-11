package ru.surfstudio.android.core.mvp.binding.react.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHubHolder
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.reactor.Feature
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView

abstract class BaseReactActivity : BaseRxActivityView(), EventHubHolder {

    abstract override val hub: RxEventHub

    abstract fun getFeatures(): Array<out Feature>

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        getFeatures().forEach { hub.observeEvents() bindTo it::react }
    }

    fun <T> Observable<T>.sendEvent(eventTransformer: (T) -> Event) =
            this.map(eventTransformer) bindTo hub

    fun <T> Observable<T>.sendEvent(event: Event) =
            this.map { event } bindTo hub

    fun  sendEvent(event: Event) =
            hub.emitEvent(event)

}