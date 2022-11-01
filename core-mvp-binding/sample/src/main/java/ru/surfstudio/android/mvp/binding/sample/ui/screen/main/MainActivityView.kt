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

package ru.surfstudio.android.mvp.binding.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main_deprecated.*
import ru.surfstudio.android.core.mvp.binding.BaseBindableActivityView
import ru.surfstudio.android.core.mvp.binding.BindData
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.mvp.binding.sample.ui.screen.main.view.PaneView
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана.
 * Демонстрирует работу с [BindData]. Здесь непорядок с подходом. Но, если тема взлетит обещаю исправить
 * @author Vasily Beglyanin
 */
class MainActivityView : BaseBindableActivityView<MainScreenModel>() {

    override fun onBind(sm: MainScreenModel) {

        sm.solved.observe(this) {
            Toast
                .makeText(
                    this,
                    if (it) R.string.win_message else R.string.lose_message,
                    Toast.LENGTH_SHORT
                )
                .apply {
                    show()
                }
        }

        observeAndApply(sm.panel1) { observePane(pane_1, it) }
        observeAndApply(sm.panel2) { observePane(pane_2, it) }
        observeAndApply(sm.panel3) { observePane(pane_3, it) }
        observeAndApply(sm.panel4) { observePane(pane_4, it) }
        observeAndApply(sm.panel5) { observePane(pane_5, it) }
        observeAndApply(sm.panel6) { observePane(pane_6, it) }
        observeAndApply(sm.panel7) { observePane(pane_7, it) }
        observeAndApply(sm.panel8) { observePane(pane_8, it) }
        observeAndApply(sm.panel9) { observePane(pane_9, it) }

        pane_1.listener = { listenPane(sm.panel1, it.toInt()) }
        pane_2.listener = { listenPane(sm.panel2, it.toInt()) }
        pane_3.listener = { listenPane(sm.panel3, it.toInt()) }
        pane_4.listener = { listenPane(sm.panel4, it.toInt()) }
        pane_5.listener = { listenPane(sm.panel5, it.toInt()) }
        pane_6.listener = { listenPane(sm.panel6, it.toInt()) }
        pane_7.listener = { listenPane(sm.panel7, it.toInt()) }
        pane_8.listener = { listenPane(sm.panel8, it.toInt()) }
        pane_9.listener = { listenPane(sm.panel9, it.toInt()) }
    }

    private fun observePane(pane: PaneView, data: PaneDataModel) {
        with(pane) {
            text = data.value.toString()
            when (data.state) {
                State.PRESSED -> setPressed()
                State.UNPRESSED -> setUnpressed()
            }
        }
    }

    private fun listenPane(pane: BindData<PaneDataModel>, data: Int) {
        pane.setValue(this, pane.value.copy(value = data))
    }

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main_deprecated

    override fun getScreenName(): String = "main"

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator =
        MainScreenConfigurator(intent)

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        easy_win_btn.setOnClickListener { presenter.onEasyWinClick() }
        unbind_btn.setOnClickListener { presenter.onUnbindClick() }
    }
}
