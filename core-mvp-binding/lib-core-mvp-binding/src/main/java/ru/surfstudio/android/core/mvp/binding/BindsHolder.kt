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

/**
 * Вспомогательный класс для управления подписками/слушателями
 */
@Deprecated("Используйте пакет ru.surfstudio.android.core.mvp.binding.rx")
class BindsHolder(private val source: Any) {

    val binds: MutableSet<BindData<*>> = mutableSetOf()

    fun <T> observe(bindData: BindData<T>, listener: (T) -> Unit) {
        bindData.observe(source, listener)
        binds.add(bindData)
    }

    fun <T> observeAndApply(bindData: BindData<T>, listener: (T) -> Unit) {
        bindData.observeAndApply(source, listener)
        binds.add(bindData)
    }

    fun unObserve() {
        binds.forEach { it.unObserveSource(source) }
    }
}