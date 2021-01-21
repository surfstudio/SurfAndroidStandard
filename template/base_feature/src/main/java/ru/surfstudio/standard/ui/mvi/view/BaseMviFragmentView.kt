package ru.surfstudio.standard.ui.mvi.view

import android.os.Bundle
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.ui.BaseReactFragmentView

/**
 * Базовый Fragment для reduce-подхода mvi с поддержкой одного типа state [S] и event [E].
 *
 * Для корректной работы необходимо переопределить:
 * * [hub] - EventHub<[E]>
 * * [sh] - StateHolder<[S]>
 * * [render] - отрисовка стейта [S]
 */
abstract class BaseMviFragmentView<S, E : Event> : BaseReactFragmentView(), SingleStateView<S>,
        SingleHubOwner<E> {

    abstract override var hub: ScreenEventHub<E>

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        initViews()
        bind()
    }

    @CallSuper
    protected open fun bind() {
        sh bindTo ::render
    }
}
