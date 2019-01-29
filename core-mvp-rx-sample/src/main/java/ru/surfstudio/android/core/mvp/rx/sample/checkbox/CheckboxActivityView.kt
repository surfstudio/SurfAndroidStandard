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

package ru.surfstudio.android.core.mvp.rx.sample.checkbox

import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.checkedChanges
import kotlinx.android.synthetic.main.activity_checkboxes.*
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.sample.R
import javax.inject.Inject

/**
 * Экран демострирующий возможность работы со связными данными напримере чекбоксов
 */
class CheckboxActivityView : BaseRxActivityView<CheckboxViewBinding>() {

    @Inject
    lateinit var presenter: CheckboxPresenter

    override fun bind(vb: CheckboxViewBinding) {
        checkbox_1.checkedChanges() bindTo vb.checkAction1
        checkbox_2.checkedChanges() bindTo vb.checkAction2
        checkbox_3.checkedChanges() bindTo vb.checkAction3
        send_btn.clicks() bindTo vb.sendAction

        vb.count bindTo { counter_et.text = it.toString() }

        vb.messageCommand bindTo { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
    }

    override fun createConfigurator() = CheckboxScreenConfigurator(intent)

    override fun getScreenName(): String = "CheckboxActivityView"

    override fun getContentView(): Int = R.layout.activity_checkboxes

    override fun getPresenters() = arrayOf(presenter)
}
