package ru.surfstudio.android.core.mvi.impls.ui.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import ru.surfstudio.android.core.mvi.ui.SingleStateView

/**
 * Базовая Activity для reduce-подхода mvi с поддержкой одного типа state [S] и event [E].
 *
 * Для корректной работы необходимо переопределить:
 * * [hub] - EventHub<[E]>
 * * [sh] - StateHolder<[S]>
 * * [render] - отрисовка стейта [S]
 */
abstract class BaseMviActivityView<S, E : Event> : BaseReactActivityView(), SingleStateView<S>, SingleHubOwner<E> {

    abstract override var hub: ScreenEventHub<E>

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        initViews()
        sh bindTo ::render
    }
}
