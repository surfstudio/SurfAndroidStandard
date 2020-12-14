package ru.surfstudio.standard.ui.mvi.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView

/**
 * Базовая Activity для reduce-подхода mvi с поддержкой одного типа state [S] и event [E].
 *
 * Для корректной работы необходимо переопределить:
 * * [hub] - EventHub<[E]>
 * * [sh] - StateHolder<[S]>
 * * [render] - отрисовка стейта [S]
 */
abstract class BaseMviActivityView<S, E : Event> : BaseReactActivityView(), SingleStateView<S>,
        SingleHubOwner<E> {

    abstract override var hub: ScreenEventHub<E>

    @CallSuper
    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        initViews()
        bind()
    }

    @CallSuper
    protected open fun bind() {
        sh bindTo ::render
    }

}
