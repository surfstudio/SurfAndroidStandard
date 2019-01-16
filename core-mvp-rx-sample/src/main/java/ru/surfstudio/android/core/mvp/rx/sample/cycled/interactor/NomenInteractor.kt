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

package ru.surfstudio.android.core.mvp.rx.sample.cycled.interactor

import io.reactivex.Single
import ru.surfstudio.android.core.mvp.rx.sample.cycled.domen.Origin
import ru.surfstudio.android.core.mvp.rx.sample.cycled.domen.UnknownOrigin
import ru.surfstudio.android.dagger.scope.PerApplication

@PerApplication
class NomenInteractor {

    fun detectOrgin(nomen: String): Single<Origin> = Single.fromCallable {
        Origin.allOrigins
                .firstOrNull { origin ->
                    origin.suffixes.any { suffix ->
                        nomen.endsWith(suffix, true)
                    }
                }
                ?: UnknownOrigin()
    }

    fun composeNomen(nomenBase: String, origin: Origin) = Single.fromCallable {
        nomenBase + origin.defaultSuffix
    }

    fun extractBaseOfNomen(fullnomen: String, origin: Origin) = Single.fromCallable {
        origin.suffixes
                .firstOrNull { suffix -> fullnomen.endsWith(suffix, true) }
                ?.let { suffix -> fullnomen.removeSuffix(suffix) }
                ?: fullnomen
    }

}