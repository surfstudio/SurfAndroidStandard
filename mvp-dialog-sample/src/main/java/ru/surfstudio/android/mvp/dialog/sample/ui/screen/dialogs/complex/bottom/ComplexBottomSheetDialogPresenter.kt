package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.bottom

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event.DataChangedEvent
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event.DataChangedEventType
import ru.surfstudio.android.rxbus.RxBus
import javax.inject.Inject

@PerScreen
internal class ComplexBottomSheetDialogPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                                     private val route: ComplexBottomSheetDialogRoute,
                                                                     private val dialogNavigator: DialogNavigator,
                                                                     private val rxBus: RxBus
) : BasePresenter<ComplexBottomSheetDialogFragment>(basePresenterDependency) {

    private val screenModel: ComplexBottomSheetDialogScreenModel = ComplexBottomSheetDialogScreenModel(route.sampleData)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun increment() {
        screenModel.sampleData.increment()
        view.render(screenModel)
    }

    fun decrement() {
        screenModel.sampleData.decrement()
        view.render(screenModel)
    }

    fun applyChanges() {
        rxBus.emitEvent(DataChangedEvent(screenModel.sampleData, DataChangedEventType.COMPLEX_BOTTOM_SHEET_DIALOG))
        dialogNavigator.dismiss(route)
    }
}