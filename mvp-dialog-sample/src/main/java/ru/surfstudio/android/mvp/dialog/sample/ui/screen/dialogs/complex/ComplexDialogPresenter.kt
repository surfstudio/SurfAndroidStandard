package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import javax.inject.Inject

class ComplexDialogPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 route: ComplexDialogRoute
) : BasePresenter<ComplexDialogFragment>(basePresenterDependency) {

    private var screenModel: ComplexDialogScreenModel = ComplexDialogScreenModel(route.sampleData)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}