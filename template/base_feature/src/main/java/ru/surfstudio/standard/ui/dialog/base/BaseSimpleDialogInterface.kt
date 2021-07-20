package ru.surfstudio.standard.ui.dialog.base

/**
 * Базовый интерфейс диалогов, возвращающих простой результат
 */
interface BaseSimpleDialogInterface : CoreSimpleDialog {

    var result: SimpleResult

    val route: BaseSimpleDialogRoute

    fun dismiss()

    fun closeWithResult(result: SimpleResult) {
        this.result = result
        dismiss()
    }
}
