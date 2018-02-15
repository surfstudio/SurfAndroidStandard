package com.example.standarddialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import ru.surfstudio.android.core.ui.base.screen.dialog.BaseSimpleDialogFragment
import javax.inject.Inject


/**
 * Фрагмент стандартного диалога
 */
class StandardDialog : BaseSimpleDialogFragment() {

    @Inject
    lateinit var presenter: StandardDialogPresenter

    private lateinit var route: StandardDialogRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getScreenComponent(StandardDialogComponent::class.java).inject(this)
        route = StandardDialogRoute(arguments!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.context, theme)
        return builder
                .setTitle(route.title)
                .setMessage(route.message)
                .setNegativeButton(route.negativeBtnText, { _, _ ->
                    presenter.negativeBtnAction(dialogTag = route.tagConst)
                    dismiss()
                })
                .setPositiveButton(route.possitiveBtnText, { _, _ -> presenter.positiveBtnAction(route.tagConst) })
                .setCancelable(route.isCancelable)
                .create()
    }

    override fun getName(): String = "Simple Dialog"

}