package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.bus.RxBus
import ru.surfstudio.android.logger.Logger
import javax.inject.Inject

class ComplexDialogPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 route: ComplexDialogRoute,
                                                 private val rxBus: RxBus
) : BasePresenter<ComplexDialogFragment>(basePresenterDependency) {

    private var screenModel: ComplexDialogScreenModel = ComplexDialogScreenModel(route.sampleData)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        Logger.d("AAA ComplexDialogPresenter onLoad")
        view.render(screenModel)
    }
}