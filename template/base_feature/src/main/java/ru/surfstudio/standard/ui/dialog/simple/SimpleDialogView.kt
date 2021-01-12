package ru.surfstudio.standard.ui.dialog.simple

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.mvi.impls.ui.dialog.standard.EMPTY_RES
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.rx.extension.observeScreenResult
import ru.surfstudio.standard.ui.dialog.base.BaseSimpleDialogView
import ru.surfstudio.standard.ui.dialog.base.SimpleResult

/**
 * Диалог позволяющий получить [SimpleResult]
 * Для получения результата использовать [observeScreenResult] у [ScreenResultObserver]
 */
class SimpleDialogView : BaseSimpleDialogView() {

    override lateinit var route: SimpleDialogRoute

    lateinit var title: String
    lateinit var message: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        route = SimpleDialogRoute(requireArguments())
        isCancelable = route.isCancelable

        title = if (route.titleRes != EMPTY_RES) getString(route.titleRes) else route.title
        message = if (route.messageRes != EMPTY_RES) getString(route.messageRes) else route.message

        return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonSafe()
                .setNegativeButtonSafe()
                .create()
                .apply {
                    // paint button only after the dialog is shown
                    setOnShowListener {
                        setButtonColorSafe(AlertDialog.BUTTON_POSITIVE, route.positiveBtnColorRes)
                        setButtonColorSafe(AlertDialog.BUTTON_NEGATIVE, route.negativeBtnColorRes)
                    }
                }
    }

    private fun AlertDialog.Builder.setPositiveButtonSafe(): AlertDialog.Builder {
        val textResId: Int = route.positiveBtnTextRes
        val resultListener = createResultListener(SimpleResult.POSITIVE)
        return if (textResId != EMPTY_RES) setPositiveButton(textResId, resultListener) else this
    }

    private fun AlertDialog.Builder.setNegativeButtonSafe(): AlertDialog.Builder {
        val textResId: Int = route.negativeBtnTextRes
        val resultListener = createResultListener(SimpleResult.NEGATIVE)
        return if (textResId != EMPTY_RES) setNegativeButton(textResId, resultListener) else this
    }

    private fun AlertDialog.setButtonColorSafe(buttonType: Int, @ColorRes colorResId: Int) {
        if (colorResId != EMPTY_RES) {
            val color = ContextCompat.getColor(context, colorResId)
            getButton(buttonType)?.apply {
                setTextColor(color)
                isAllCaps = false
            }
        }
    }

    private fun createResultListener(result: SimpleResult): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _, _ ->
            closeWithResult(result)
        }
    }
}
