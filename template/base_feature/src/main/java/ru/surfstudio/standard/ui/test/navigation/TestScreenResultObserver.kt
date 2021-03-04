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
package ru.surfstudio.standard.ui.test.navigation

import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.listener.ScreenResultListener
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.BaseRoute
import java.io.Serializable

/**
 * Stub implementation of [ScreenResultObserver] for testing.
 */
class TestScreenResultObserver() : ScreenResultObserver {

    override fun <T : Serializable, R> addListener(
            targetRoute: R,
            listener: ScreenResultListener<T>
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        /* do nothing */
    }

    override fun <R> removeListener(targetRoute: R) where R : BaseRoute<*>, R : ResultRoute<*> {
        /* do nothing */
    }
}