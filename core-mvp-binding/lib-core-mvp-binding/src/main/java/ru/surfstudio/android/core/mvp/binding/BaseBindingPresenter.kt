/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.binding

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Вспомогательный презентер для работы с [BindData]
 */
@Deprecated("Используйте пакет ru.surfstudio.android.core.mvp.binding.rx")
abstract class BaseBindingPresenter<M : ScreenModel, V>(basePresenterDependency: BasePresenterDependency)
    : BasePresenter<V>(basePresenterDependency), BindSource
        where  V : CoreView, V : BindableView<M> {

    @Suppress("LeakingThis") //для BindData не имеет значения какой именно объект передается в качестве source
    protected val bindsHolder = BindsHolder(this)

    abstract val sm: M

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

@Deprecated("Используйте пакет ru.surfstudio.android.core.mvp.binding.rx")
interface BindSource {

    fun <T> observe(bindData: IBindData<T>, listener: (T) -> Unit)
    fun <T> observeAndApply(bindData: IBindData<T>, listener: (T) -> Unit)
}