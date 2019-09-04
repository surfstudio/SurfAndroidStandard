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
@file:JvmName("ViewContextUnwrapper")
package ru.surfstudio.android.mvp.widget.delegate

import android.content.Context
import android.view.ContextThemeWrapper


/**
 * Helps to avoid problems with ContextThemeWrapper
 * @see <a href="https://clck.ru/FRZrW">ContextThemeWrapper problem</a>
 */
fun <T> unwrapContext(context: Context, clazz: Class<T>): T {
    var unwrappedContext = context
    while (!clazz.isInstance(unwrappedContext) && unwrappedContext is ContextThemeWrapper) {
        unwrappedContext = unwrappedContext.baseContext
    }
    return clazz.cast(unwrappedContext)!!
}