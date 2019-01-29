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

package ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main

import ru.surfstudio.android.core.mvp.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.domain.MainModel
import ru.surfstudio.android.core.mvp.rx.ui.ViewBinding

/**
 * Модель главного экрана с примером easyadapter
 */
class MainViewBinding(init: MainModel) : ViewBinding {

    val openPaginationScreen = Action<Unit>()

    val carouselState = State(init.carousels)
    val hasCommercialState = State(init.hasCommercial)
    val elementsState = State(init.elements)
    val bottomCarouselState = State(init.bottomCarousel)
}

