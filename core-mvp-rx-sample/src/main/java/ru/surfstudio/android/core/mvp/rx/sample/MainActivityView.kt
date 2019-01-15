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

package ru.surfstudio.android.core.mvp.rx.sample

import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.sample.R
import javax.inject.Inject

class MainActivityView : BaseRxActivityView<MainModel>() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun bind(sm: MainModel) {

        sm.counterState.bindTo{ main_counter_tv.text = it.toString() }
        main_inc_btn.clicks().bindTo { sm.incAction.getConsumer().accept(Unit) }
        main_dec_btn.clicks().bindTo { sm.decAction.getConsumer().accept(Unit) }

        main_text_et.textChanges().map { it.toString() } bindTo {sm.textEditState.getConsumer().accept(it)}
        sm.textEditState.bindTo { main_text_et.setText(it) }
        main_double_text_btn.clicks().bindTo { sm.doubleTextAction.getConsumer().accept(Unit) }

        sm.sampleCommand bindTo { text_tv.text = it }

        checkbox_sample_btn.clicks() bindTo sm.checkboxSampleActivityOpen
        cycled_dependency_sample_btn.clicks() bindTo sm.cycledSampleActivityOpen
    }

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun getPresenters() = arrayOf(presenter)

}
