package ru.surfstudio.android.core.mvp.binding

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentView
import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Вспомогательные view для работы с [BindData]. Работают в паре с [BaseBindingPresenter]
 */

abstract class BaseBindableActivityView<in M : ScreenModel> : CoreActivityView(), BindableView<M>, BindSource {

    @Suppress("LeakingThis") //для BindData не имеет значения какой именно объект передается в качестве source
    private val bindsHolder = BindsHolder(this)

    override fun <T> observe(bindData: BindData<T>, listener: (T) -> Unit) {
        bindsHolder.observe(bindData, listener)
    }

    override fun <T> observeAndApply(bindData: BindData<T>, listener: (T) -> Unit) {
        bindsHolder.observeAndApply(bindData, listener)
    }

    @CallSuper
    override fun onUnbind(screenModel: M) {
        bindsHolder.unObserve()
    }
}


abstract class BaseBindableFragmentView<in M : ScreenModel> : CoreFragmentView(), BindableView<M>, BindSource {

    private val bindsHolder = BindsHolder(this)

    override fun <T> observe(bindData: BindData<T>, listener: (T) -> Unit) {
        bindsHolder.observe(bindData, listener)
    }

    override fun <T> observeAndApply(bindData: BindData<T>, listener: (T) -> Unit) {
        bindsHolder.observeAndApply(bindData, listener)
    }

    @CallSuper
    override fun onUnbind(screenModel: M) {
        bindsHolder.unObserve()
    }
}

interface BindableView<in M : ScreenModel> {

    fun onBind(screenModel: M)
    fun onUnbind(screenModel: M)
}
