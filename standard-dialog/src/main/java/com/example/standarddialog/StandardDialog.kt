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
        getScreenComponent(StandardDialogComponent::class.java).inject(this)
        route = StandardDialogRoute(arguments!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.context, theme)
        return builder
                .setTitle(route.getTitle(this.context!!))
                .setMessage(route.getMessage(this.context!!))
                .setNegativeButton(route.getNegativeBtnTxt(this.context!!), { _, _ ->
                    presenter.negativeBtnAction(dialogTag = route.dialogTag)
                    dismiss()
                })
                .setPositiveButton(route.getPossitiveBtnTxt(this.context!!), { _, _ ->
                    presenter.positiveBtnAction(dialogTag = route.dialogTag)
                    dismiss()
                })
                .setCancelable(route.isCancelable)
                .create()
    }

    override fun getName(): String = "Simple Dialog"


}