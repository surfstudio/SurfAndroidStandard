package ru.surfstudio.android.core.mvi.impls.ui.view

import android.os.Bundle
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.ui.BaseReactFragmentView
import ru.surfstudio.android.core.mvi.ui.SingleStateView

/**
 * Базовый Fragment для reduce-подхода mvi с поддержкой одного типа state [S] и event [E].
 *
 * Для корректной работы необходимо переопределить:
 * * [hub] - EventHub<[E]>
 * * [sh] - StateHolder<[S]>
 * * [render] - отрисовка стейта [S]
 */
abstract class BaseMviFragmentView<S, E : Event> : BaseReactFragmentView(), SingleStateView<S>, SingleHubOwner<E> {

    abstract override var hub: ScreenEventHub<E>

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        initViews()
        sh bindTo ::render
    }
}
