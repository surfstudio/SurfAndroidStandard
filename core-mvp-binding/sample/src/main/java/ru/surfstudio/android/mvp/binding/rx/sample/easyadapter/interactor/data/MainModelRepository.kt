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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.interactor.data

import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.MainModel
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.interactor.element.Elements
import ru.surfstudio.easyadapter.sample.domain.Carousel
import javax.inject.Inject

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Генерирует [MainModel]
 */
class MainModelRepository @Inject constructor() {
    private val CAROUSELS = arrayListOf(
            Carousel("aaa", "Sed condimentum"),
            Carousel("bbb", "Etiam mollis"),
            Carousel("ccc", "Blandit dolor"),
            Carousel("ddd", "Morbi ipsum"))

    private val ELEMENTS = Elements.all

    private val models: List<MainModel>
    private var current = 0

    init {
        models = arrayListOf(firstModel(),
                secondModel(), thirdModel(), fourthModel(), fifthModel(),
                sixthModel())
    }

    fun next(): MainModel {
        current++
        return models[current % models.size]
    }

    private fun firstModel(): MainModel {
        return MainModel(
                carousels = arrayListOf(
                        CAROUSELS[0].copy(elements = arrayListOf(
                                ELEMENTS[0],
                                ELEMENTS[1],
                                ELEMENTS[2],
                                ELEMENTS[3],
                                ELEMENTS[4],
                                ELEMENTS[5],
                                ELEMENTS[6],
                                ELEMENTS[7],
                                ELEMENTS[8])),
                        CAROUSELS[1].copy(elements = arrayListOf(
                                ELEMENTS[12],
                                ELEMENTS[22],
                                ELEMENTS[3],
                                ELEMENTS[7],
                                ELEMENTS[11],
                                ELEMENTS[12],
                                ELEMENTS[6],
                                ELEMENTS[1],
                                ELEMENTS[15]))),
                hasCommercial = false,
                elements = arrayListOf(
                        ELEMENTS[21],
                        ELEMENTS[23],
                        ELEMENTS[19],
                        ELEMENTS[14],
                        ELEMENTS[10]),
                bottomCarousel = emptyList())
    }

    private fun secondModel(): MainModel {
        return MainModel(
                carousels = arrayListOf(
                        CAROUSELS[1].copy(elements = arrayListOf(
                                ELEMENTS[10],
                                ELEMENTS[3],
                                ELEMENTS[22],
                                ELEMENTS[7],
                                ELEMENTS[11],
                                ELEMENTS[12],
                                ELEMENTS[6],
                                ELEMENTS[1],
                                ELEMENTS[15]))),
                hasCommercial = true,
                elements = arrayListOf(
                        ELEMENTS[21],
                        ELEMENTS[23],
                        ELEMENTS[19],
                        ELEMENTS[14],
                        ELEMENTS[10]),
                bottomCarousel = emptyList())
    }

    private fun thirdModel(): MainModel {
        return MainModel(
                carousels = arrayListOf(
                        CAROUSELS[1].copy(elements = arrayListOf(
                                ELEMENTS[3],
                                ELEMENTS[22],
                                ELEMENTS[7],
                                ELEMENTS[10],
                                ELEMENTS[11],
                                ELEMENTS[12],
                                ELEMENTS[6],
                                ELEMENTS[1],
                                ELEMENTS[15]))),
                hasCommercial = false,
                elements = arrayListOf(
                        ELEMENTS[20],
                        ELEMENTS[17],
                        ELEMENTS[19],
                        ELEMENTS[13],
                        ELEMENTS[10]),
                bottomCarousel = listOf(CAROUSELS[3].copy(
                        elements = arrayListOf(
                                ELEMENTS[12],
                                ELEMENTS[22],
                                ELEMENTS[3],
                                ELEMENTS[7],
                                ELEMENTS[11],
                                ELEMENTS[12],
                                ELEMENTS[6],
                                ELEMENTS[1],
                                ELEMENTS[15]))))
    }

    private fun fourthModel(): MainModel {
        return MainModel(
                carousels = arrayListOf(
                        CAROUSELS[1].copy(elements = arrayListOf(
                                ELEMENTS[17],
                                ELEMENTS[3],
                                ELEMENTS[7],
                                ELEMENTS[11],
                                ELEMENTS[10],
                                ELEMENTS[12],
                                ELEMENTS[6],
                                ELEMENTS[1],
                                ELEMENTS[15])),
                        CAROUSELS[0].copy(elements = arrayListOf(
                                ELEMENTS[1],
                                ELEMENTS[4],
                                ELEMENTS[12],
                                ELEMENTS[2],
                                ELEMENTS[16],
                                ELEMENTS[5],
                                ELEMENTS[6],
                                ELEMENTS[7],
                                ELEMENTS[8]))),
                hasCommercial = true,
                elements = arrayListOf(
                        ELEMENTS[19],
                        ELEMENTS[16],
                        ELEMENTS[17],
                        ELEMENTS[0],
                        ELEMENTS[23],
                        ELEMENTS[10]),
                bottomCarousel = emptyList())
    }

    private fun fifthModel(): MainModel {
        return MainModel(
                carousels = arrayListOf(
                        CAROUSELS[0].copy(elements = arrayListOf(
                                ELEMENTS[1],
                                ELEMENTS[12],
                                ELEMENTS[4],
                                ELEMENTS[5],
                                ELEMENTS[16],
                                ELEMENTS[2],
                                ELEMENTS[6],
                                ELEMENTS[7],
                                ELEMENTS[8]))),
                hasCommercial = false,
                elements = arrayListOf(
                        ELEMENTS[20],
                        ELEMENTS[16],
                        ELEMENTS[19],
                        ELEMENTS[2],
                        ELEMENTS[0],
                        ELEMENTS[10]),
                bottomCarousel = emptyList())
    }

    private fun sixthModel(): MainModel {
        return MainModel(
                carousels = emptyList(),
                hasCommercial = false,
                elements = emptyList(),
                bottomCarousel = emptyList())
    }
}