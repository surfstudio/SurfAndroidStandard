/*
  Copyright (c) 2018-present, SurfStudio LLC, Georgiy Kartashov.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.standard.ui.test.base

import io.kotest.core.spec.style.AnnotationSpec
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.standard.base.test.TestErrorHandler
import ru.surfstudio.standard.base.test.TestScreenState
import ru.surfstudio.standard.base.test.TestResourceProvider
import ru.surfstudio.standard.base.test.TestSchedulersProvider
import ru.surfstudio.standard.ui.test.mvi.TestNavigationMiddleware
import ru.surfstudio.android.core.ui.state.ScreenState

/**
 * Base class for Middleware-entity test cases.
 */
abstract class BaseMiddlewareTest : AnnotationSpec(), StateEmitter {

    protected val resourceProvider: ResourceProvider = TestResourceProvider()
    protected val schedulerProvider: SchedulersProvider = TestSchedulersProvider()
    protected val errorHandler: ErrorHandler = TestErrorHandler()
    protected val screenState: ScreenState = TestScreenState()
    protected val baseMiddlewareDependency = BaseMiddlewareDependency(
        schedulerProvider,
        errorHandler,
        screenState
    )
    protected val navigationMiddleware = TestNavigationMiddleware()

    protected fun <S, SH : State<S>> SH.resetState(initialState: S) {
        accept(initialState)
    }
}
