package ru.surfstudio.android.core.mvp.binding

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Вспомогательный презентер для работы с [BindData]
 */
abstract class BaseBindingPresenter<M : ScreenModel, V>(basePresenterDependency: BasePresenterDependency)
    : BasePresenter<V>(basePresenterDependency), BindSource
        where  V : CoreView, V : BindableView<M> {

    @Suppress("LeakingThis") //для BindData не имеет значения какой именно объект передается в качестве source
    protected val bindsHolder = BindsHolder(this)

    abstract val sm: M

    @CallSuper
    override fun onFirstLoad() {
        super.onFirstLoad()
        view.onBind(sm)
    }

    override fun onViewDetach() {
        super.onViewDetach()
        view?.onUnbind(sm)
    }

    override fun onDestroy() {
        super.onDestroy()
        bindsHolder.unObserve()
    }

    override fun <T> observe(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindsHolder.observe(bindData, listener)
    }

    override fun <T> observeAndApply(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindsHolder.observeAndApply(bindData, listener)
    }
}

interface BindSource {

    fun <T> observe(bindData: IBindData<T>, listener: (T) -> Unit)
    fun <T> observeAndApply(bindData: IBindData<T>, listener: (T) -> Unit)
}