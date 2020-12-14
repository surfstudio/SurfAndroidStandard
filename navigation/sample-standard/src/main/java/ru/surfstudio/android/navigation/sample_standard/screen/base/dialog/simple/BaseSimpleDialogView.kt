package ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple

import android.content.DialogInterface
import androidx.annotation.CallSuper
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult

/**
 * Базовый простой диалог с возможностью возвращения результата.
 *
 * @property result результат работы экрана. Эмитится при его закрытии.
 * @property route route, служащая для открытия экрана, и для оповещения о результате.
 */
abstract class BaseSimpleDialogView : CoreSimpleDialog() {

    abstract val route: BaseSimpleDialogRoute

    private var result = SimpleDialogResult.DISMISS

    protected fun closeWithResult(result: SimpleDialogResult) {
        this.result = result
        dismiss()
    }

    @CallSuper
    override fun onDismiss(dialog: DialogInterface) {
        getCommandExecutor().execute(EmitScreenResult(route, result))
        super.onDismiss(dialog)
    }
}
