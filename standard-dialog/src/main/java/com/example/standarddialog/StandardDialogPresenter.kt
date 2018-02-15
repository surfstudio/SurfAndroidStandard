package com.example.standarddialog

/**
 * Интерфейс, которому должен удовлетворять любой презентер, использующий стандартный диалог
 */
interface StandardDialogPresenter {
    fun positiveBtnAction(dialogTag: String)
    fun negativeBtnAction(dialogTag: String)
}