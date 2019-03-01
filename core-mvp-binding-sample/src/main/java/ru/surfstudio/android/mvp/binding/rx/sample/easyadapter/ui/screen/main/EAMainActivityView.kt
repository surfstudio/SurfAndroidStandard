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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.activity_easy_adapter_sample.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.recycler.animator.SlideItemAnimator
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.recycler.controller.ElementController
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.recycler.controller.EmptyStateController
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.list.CommercialController
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.list.DeliveryController
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.list.HeaderController
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.list.carousel.CarouselController
import ru.surfstudio.android.utilktx.data.wrapper.selectable.SelectableData
import ru.surfstudio.easyadapter.sample.domain.Carousel
import javax.inject.Inject

/**
 * example screen with list with different types of items
 */
class EAMainActivityView : BaseRxActivityView() {

    @Inject
    lateinit var bm: MainBindModel

    private lateinit var headerController: HeaderController
    private lateinit var carouselController: CarouselController
    private lateinit var deliveryController: DeliveryController
    private lateinit var commercialController: CommercialController
    private lateinit var elementController: ElementController
    private lateinit var emptyStateController: EmptyStateController

    private val adapter = EasyAdapter()

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> =
            EaMainScreenConfigurator(intent)

    override fun getScreenName(): String = "Main easy adapter sample"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        bind()

        val linearLayoutManager = LinearLayoutManager(this)
        val itemAnimator = SlideItemAnimator()
        recycler.itemAnimator = itemAnimator
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = adapter
    }

    fun bind() {
        headerController = HeaderController()

        carouselController = CarouselController(
                onElementClickListener = { bm.openPaginationScreen.accept(Unit) },
                onShowAllClickListener = { bm.openPaginationScreen.accept(Unit) })
        deliveryController = DeliveryController { bm.openPaginationScreen.accept(Unit) }
        elementController = ElementController { bm.openPaginationScreen.accept(Unit) }

        //Также можно использовать реактивные интерфейсы (здесь RxClickable)
        commercialController = CommercialController().apply {
            clicks() bindTo bm.openPaginationScreen
        }

        emptyStateController = EmptyStateController()

        Observables.combineLatest(
                bm.carouselState.observable,
                bm.elementsState.observable,
                bm.bottomCarouselState.observable,
                bm.hasCommercialState.observable,
                ::createItemList
        ) bindTo adapter::setItems
    }

    private fun createItemList(carousels: List<Carousel>, elements: List<Element>, bottomCarousel: List<Carousel>, hasCommercial: Boolean): ItemList {
        val isEmpty = carousels.isEmpty() && !hasCommercial && elements.isEmpty() && bottomCarousel.isEmpty()
        return ItemList.create()
                .addIf(carousels.isNotEmpty(), headerController)
                .addAll(carousels, carouselController)
                .addIf(!isEmpty, deliveryController)
                .addIf(hasCommercial, commercialController)
                .addAll(elements.map { SelectableData(it) }, elementController)
                .addAll(bottomCarousel, carouselController)
                .addIf(isEmpty, emptyStateController)
    }

    override fun getContentView(): Int = R.layout.activity_easy_adapter_sample
}
