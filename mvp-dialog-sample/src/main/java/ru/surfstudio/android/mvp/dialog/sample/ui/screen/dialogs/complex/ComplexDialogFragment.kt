package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.mvp.dialog.complex.CoreDialogFragmentView
import javax.inject.Inject

class ComplexDialogFragment : CoreDialogFragmentView() {

    @Inject
    internal lateinit var presenter: ComplexDialogPresenter

    override fun getPresenters(): Array<CorePresenter<CoreView>> {
        TODO()
    }

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> {
        return ComplexDialogScreenConfigurator(arguments!!)
    }

    override fun getScreenName(): String = "Complex Dialog Fragment"

    fun render(screenModel: ComplexDialogScreenModel) {

    }
}