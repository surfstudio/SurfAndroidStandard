/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.example.standarddialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment
import javax.inject.Inject


/**
 * Фрагмент стандартного диалога
 */
class StandardDialog : CoreSimpleDialogFragment() {

    @Inject
    lateinit var presenter: StandardDialogPresenter

    private lateinit var route: StandardDialogRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        route = StandardDialogRoute(arguments!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.context, theme)
        val dialog:Dialog = builder
                .setTitle(route.getTitle(this.context!!))
                .setMessage(route.getMessage(this.context!!))
                .setNegativeButton(route.getNegativeBtnTxt(this.context!!), { _, _ ->
                    inject()
                    presenter.simpleDialogNegativeBtnAction(dialogTag = route.dialogTag)
                    dismiss()
                })
                .setPositiveButton(route.getPositiveBtnTxt(this.context!!), { _, _ ->
                    inject()
                    presenter.simpleDialogPositiveBtnAction(dialogTag = route.dialogTag)
                    dismiss()
                })
                .create()
        dialog.setCanceledOnTouchOutside(route.isCancelable)
        return dialog
    }

    override fun getName(): String = "StandardDialog"

    private fun inject() {
        getScreenComponent(StandardDialogComponent::class.java).inject(this)
    }
}