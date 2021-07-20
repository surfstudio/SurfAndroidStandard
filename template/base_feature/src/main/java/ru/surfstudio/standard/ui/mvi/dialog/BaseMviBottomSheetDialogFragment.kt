package ru.surfstudio.standard.ui.mvi.dialog

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxBottomSheetDialogFragment
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Базовый класс для Bottom Sheet диалога с сущностями MVi
 */
abstract class BaseMviBottomSheetDialogFragment : BaseRxBottomSheetDialogFragment() {

    override fun getPresenters(): Array<CorePresenter<CoreView>> {
        return emptyArray()
    }
}