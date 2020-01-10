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

package ru.surfstudio.android.mvp.binding.rx.sample

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.mvp.binding.sample.R
import javax.inject.Inject

/**
 * Главный экран примеров
 */
class MainActivityView : BaseRxActivityView() {

    @Inject
    lateinit var bm: MainBindModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        bind()
    }

    fun bind() {
        bm.counterBond.observable.map { it.toString() } bindTo main_counter_tv::setText
        bm.textEditBond bindTo main_text_et::setText
        bm.sampleState bindTo text_tv::setText

        bm.messageCommand bindTo ::showMessage

        main_text_et.textChanges().map { it.toString() } bindTo bm.textEditBond

        main_inc_btn.clicks() bindTo bm.incAction
        main_dec_btn.clicks() bindTo bm.decAction
        main_double_text_btn.clicks() bindTo bm.doubleTextAction

        dialog_sample_btn.clicks() bindTo bm.dialogOpenAction
        checkbox_sample_btn.clicks() bindTo bm.checkboxSampleActivityOpen
        easy_adapter_sample_btn.clicks() bindTo bm.easyadapterSampleActivityOpen
    }

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
