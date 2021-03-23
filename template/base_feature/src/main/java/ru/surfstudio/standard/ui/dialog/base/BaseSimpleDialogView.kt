package ru.surfstudio.standard.ui.dialog.base

import android.content.DialogInterface
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult

/**
 * Базовый простой диалог с возможностью возвращения результата.
 *
 * @property result результат работы экрана. Эмитится при его закрытии.
 * @property route route, служащая для открытия экрана, и для оповещения о результате.
 */
abstract class BaseSimpleDialogView : DialogFragment(), BaseSimpleDialogInterface {

    override var result: SimpleResult = SimpleResult.DISMISS

    @CallSuper
    override fun onDismiss(dialog: DialogInterface) {
        getCommandExecutor().execute(EmitScreenResult(route, result))
        super.onDismiss(dialog)
    }
}
