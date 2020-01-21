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

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentView
import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Вспомогательные view для работы с [IBindData]. Работают в паре с [BaseBindingPresenter]
 */
abstract class BaseBindableActivityView<in M : ScreenModel> : CoreActivityView(), BindableView<M>, BindSource {

    @Suppress("LeakingThis") //для BindData не имеет значения какой именно объект передается в качестве source
    private val bindsHolder = BindsHolder(this)

    override fun <T> observe(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindsHolder.observe(bindData, listener)
    }

    override fun <T> observeAndApply(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindsHolder.observeAndApply(bindData, listener)
    }

    @CallSuper
    override fun onUnbind(sm: M) {
        bindsHolder.unObserve()
    }
}

abstract class BaseBindableFragmentView<in M : ScreenModel> : CoreFragmentView(), BindableView<M>, BindSource {

    private val bindsHolder = BindsHolder(this)

    override fun <T> observe(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindsHolder.observe(bindData, listener)
    }

    override fun <T> observeAndApply(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindsHolder.observeAndApply(bindData, listener)
    }

    @CallSuper
    override fun onUnbind(sm: M) {
        bindsHolder.unObserve()
    }
}

interface BindableView<in M : ScreenModel> {

    fun onBind(sm: M)
    fun onUnbind(sm: M)
}
