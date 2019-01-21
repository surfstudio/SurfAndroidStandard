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

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_easy_adapter_sample.*
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.animator.SlideItemAnimator
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.ElementController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.EmptyStateController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.data.MainScreenModel
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.list.CommercialController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.list.DeliveryController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.list.HeaderController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.list.carousel.CarouselController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.pagination.PaginationActivityView
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.sample.R
import javax.inject.Inject

/**
 * example screen with list with different types of items
 */
class EAMainActivityView : BaseRxActivityView<MainScreenModel>() {

    @Inject
    lateinit var presenter: EAMainPresenter

    private lateinit var headerController: HeaderController
    private lateinit var carouselController: CarouselController
    private lateinit var deliveryController: DeliveryController
    private lateinit var commercialController: CommercialController
    private lateinit var elementController: ElementController
    private lateinit var emptyStateController: EmptyStateController

    private val adapter = EasyAdapter()

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPresenters() = arrayOf(presenter)

    override fun getScreenName(): String = "Main easy adapter sample"

    override fun bind(sm: MainScreenModel) {
        val linearLayoutManager = LinearLayoutManager(this)
        val itemAnimator = SlideItemAnimator()
        recycler.itemAnimator = itemAnimator
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = adapter

        headerController = HeaderController()
        carouselController = CarouselController(
                onElementClickListener = { openPaginationScreen() },
                onShowAllClickListener = { openPaginationScreen() })
        deliveryController = DeliveryController(
                onClickListener = { openPaginationScreen() })
        commercialController = CommercialController(
                onClickListener = { openPaginationScreen() })
        elementController = ElementController(
                onClickListener = { openPaginationScreen() })
        emptyStateController = EmptyStateController()
    }

    private fun openPaginationScreen() {
        //todo handle through presenter
        //see e.g. https://github.com/MaksTuev/real_mvp_part1/blob/master/app/src/main/java/com/agna/realmvp/realmvpsample/ui/screen/splash/SplashPresenter.java
        startActivity(Intent(this, PaginationActivityView::class.java))
    }

    fun render(screenModel: MainScreenModel) {
        val itemList = ItemList.create()
                .addIf(screenModel.hasHeader(), headerController)
                .addAll(screenModel.carousels, carouselController)
                .addIf(!screenModel.isEmpty(), deliveryController)
                .addIf(screenModel.hasCommercial, commercialController)
                .addAll(screenModel.elements, elementController)
                .addIf(screenModel.hasBottomCarousel(), screenModel.bottomCarousel, carouselController)
                .addIf(screenModel.isEmpty(), emptyStateController)
        adapter.setItems(itemList)
    }

    override fun getContentView(): Int = R.layout.activity_main
}
