package ru.surfstudio.android.core.mvp.binding

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

    abstract val screenModel: M

    override fun attachView(view: V) {
        super.attachView(view)
        view.onBind(screenModel)
    }

    override fun onViewDetach() {
        super.onViewDetach()
        view.onUnbind(screenModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        bindsHolder.unObserve()
    }

    override fun <T> observe(bindData: BindData<T>, listener: (T) -> Unit) {
        bindsHolder.observe(bindData, listener)
    }

    override fun <T> observeAndApply(bindData: BindData<T>, listener: (T) -> Unit) {
        bindsHolder.observeAndApply(bindData, listener)
    }
}

interface BindSource {

    fun <T> observe(bindData: BindData<T>, listener: (T) -> Unit)
    fun <T> observeAndApply(bindData: BindData<T>, listener: (T) -> Unit)
}