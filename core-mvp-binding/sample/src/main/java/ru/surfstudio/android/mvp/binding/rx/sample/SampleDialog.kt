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

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.jakewharton.rxbinding2.widget.textChanges
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxSimpleDialogFragment
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import javax.inject.Inject

/**
 * Диалог с примером работы c [BindModel]
 */
class SampleDialog : BaseRxSimpleDialogFragment() {

    @Inject
    lateinit var vb: SampleDialogBindModel

    override fun getName(): String = "Sample Dialog"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        val builder = AlertDialog.Builder(context, theme)
        inject()

        val editText = EditText(context).apply {
            textChanges().map { it.toString() } bindTo vb.dialogInputAction
        }

        return builder
                .setTitle("Sample Dialog")
                .setMessage("Sample text")
                .setView(editText)
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    vb.dialogNegativeAction.accept()
                    dismiss()
                }
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    vb.dialogPositiveAction.accept()
                    dismiss()
                }
                .create()
    }

    private fun inject() {
        getScreenComponent(SampleDialogComponent::class.java).inject(this)
    }

}

/**
 * Интерфейс для расширения компоненты экрана
 */
interface SampleDialogComponent {

    fun inject(d: SampleDialog)
}

/**
 * Роут открытия диалога
 */
class SampleDialogRoute : DialogRoute() {

    override fun getFragmentClass() = SampleDialog::class.java
}

/**
 * Интерфейс для общения с презентором
 */
interface SampleDialogBindModel : BindModel {

    val dialogPositiveAction: Action<Unit>
    val dialogNegativeAction: Action<Unit>
    val dialogInputAction: Action<String>
}
