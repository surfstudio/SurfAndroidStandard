package ru.surfstudio.android.core.mvp.binding

import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentView
import ru.surfstudio.android.core.mvp.model.ScreenModel


/**
 * Вспомогательные view для работы с [BindData]. Работают в паре с [BaseBindingPresenter]
 */

abstract class BaseBindableActivityView<in M: ScreenModel>: CoreActivityView(), BindableView<M>

abstract class BaseBindableFragmentView<in M: ScreenModel>: CoreFragmentView(), BindableView<M>

interface BindableView<in M: ScreenModel>  {

    fun onBind(screenModel: M)
    fun onUnbind(screenModel: M)
}
