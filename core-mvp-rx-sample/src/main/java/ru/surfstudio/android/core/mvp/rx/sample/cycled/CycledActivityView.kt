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

package ru.surfstudio.android.core.mvp.rx.sample.cycled

import android.widget.RadioButton
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_cycled.*
import ru.surfstudio.android.core.mvp.rx.sample.cycled.domen.*
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.sample.R
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Экран демострирующий возможность работы со связными данными типа чекбоксов
 */
class CycledActivityView : BaseRxActivityView<CycledScreenModel>() {

    @Inject
    lateinit var presenter: CycledPresenter

    override fun bind(pm: CycledScreenModel) {
        val checkboxObserver = Observable.mergeArray(
                origin_rome_rb.prepare(),
                origin_sab_oksk_rb.prepare(),
                origin_umbrian_rb.prepare(),
                origin_etruscan_rb.prepare(),
                origin_other_rb.prepare())

        with(pm) {
            origin.observable.filter { !it.sources.contains(Source.ORIGIN) }.map { it.value } bindTo ::checkRb
            nomen.observable.filter { !it.sources.contains(Source.NOMEN) }.map { it.value as CharSequence } bindTo nomen_et::setText
            baseOfNomen.observable.filter { !it.sources.contains(Source.BASE_OF_NOMEN) }.map { it.value } bindTo nomen_base_et::setText

            nomen_base_et.textChanges()
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .map(CharSequence::toString)
                    .map { SourcedValue(setOf(Source.BASE_OF_NOMEN), it) } bindTo baseOfNomen
            nomen_et.textChanges()
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .map(CharSequence::toString)
                    .map { SourcedValue(setOf(Source.NOMEN), it) } bindTo nomen
            checkboxObserver.map { SourcedValue(setOf(Source.ORIGIN), it) } bindTo origin
        }
    }

    override fun createConfigurator() = CycledScreenConfigurator(intent)

    override fun getScreenName(): String = "CycledActivityView"

    override fun getContentView(): Int = R.layout.activity_cycled

    override fun getPresenters() = arrayOf(presenter)

    private fun RadioButton.prepare(): Observable<Origin> =
            this.checkedChanges().filter { it }.map {
                when (this.id) {
                    origin_rome_rb.id -> RomeOrigin()
                    origin_umbrian_rb.id -> UmbrianOrigin()
                    origin_sab_oksk_rb.id -> SOskanOrigin()
                    origin_etruscan_rb.id -> EtruscanOrigin()
                    else -> UnknownOrigin()
                }
            }

    private fun checkRb(origin: Origin): Unit = when (origin) {
        is RomeOrigin -> origin_rome_rb
        is SOskanOrigin -> origin_sab_oksk_rb
        is UmbrianOrigin -> origin_umbrian_rb
        is EtruscanOrigin -> origin_etruscan_rb
        is UnknownOrigin -> origin_other_rb
    }.run {
        isChecked = true
    }
}
