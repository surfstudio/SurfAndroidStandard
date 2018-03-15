package com.example.standarddialog

/**
 * Интерфейс, которому должен удовлетворять любой презентер, использующий стандартный диалог
 */
interface StandardDialogPresenter {
    fun simpleDialogPositiveBtnAction(dialogTag: String)
    fun simpleDialogNegativeBtnAction(dialogTag: String)
}