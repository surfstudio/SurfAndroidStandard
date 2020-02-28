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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.interactor.element

import io.reactivex.Observable
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

val DEFAULT_PAGE = 1

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Генерирует [Element]
 */
@PerScreen
class ElementRepository @Inject constructor() {

    private val DEFAULT_PAGE_SIZE = 20
    private val rnd = Random()

    fun getElements(page: Int): Observable<DataList<Element>> {
        val startIndex = (page - 1) * DEFAULT_PAGE_SIZE
        val endIndex = if ((startIndex + DEFAULT_PAGE_SIZE) >= Elements.all.size)
            Elements.all.size else
            startIndex + DEFAULT_PAGE_SIZE
        return Observable.timer(2500, TimeUnit.MILLISECONDS) //imitate delay
                .flatMap { _ ->
                    if (rnd.nextFloat() > 0.5f)
                        Observable.error(RuntimeException()) else //imitate error
                        Observable.just(
                                DataList(
                                        Elements.all.subList(startIndex, endIndex),
                                        page,
                                        DEFAULT_PAGE_SIZE
                                ))
                }
    }
}