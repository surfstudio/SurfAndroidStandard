package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event

import ru.surfstudio.android.mvp.dialog.sample.domain.SampleData

/**
 * Событие, которое возвращает ComplexDialogPresenter
 */
data class DataChangedEvent(val sampleData: SampleData,
                            val eventType: DataChangedEventType)