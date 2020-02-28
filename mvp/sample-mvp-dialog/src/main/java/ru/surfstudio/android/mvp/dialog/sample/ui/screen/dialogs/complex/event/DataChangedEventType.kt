package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event

/**
 * Тип события, позволяющий отличить, от какого из ComplexDialogs пришло данное событие
 */
enum class DataChangedEventType {
    COMPLEX_DIALOG,
    COMPLEX_BOTTOM_SHEET_DIALOG
}