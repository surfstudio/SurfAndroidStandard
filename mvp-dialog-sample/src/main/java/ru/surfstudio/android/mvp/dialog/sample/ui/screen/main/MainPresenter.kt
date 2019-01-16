package ru.surfstudio.android.mvp.dialog.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.sample.domain.SampleData
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.ComplexDialogRoute
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.bottom.ComplexBottomSheetDialogRoute
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event.DataChangedEvent
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.SimpleDialogPresenter
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.SimpleDialogRoute
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom.SimpleBottomSheetDialogPresenter
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom.SimpleBottomSheetDialogRoute
import ru.surfstudio.android.rxbus.RxBus
import javax.inject.Inject

const val INITIAL_COMPLEX_DIALOG_VALUE = 10

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val dialogNavigator: DialogNavigator,
                                                 private val rxBus: RxBus
) : BasePresenter<MainActivityView>(basePresenterDependency),
        SimpleDialogPresenter, SimpleBottomSheetDialogPresenter {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
        subscribe(rxBus.observeEvents(DataChangedEvent::class.java)) {
            view.showMessage("New value = ${it.sampleData.value} from ${it.eventType.name}")
        }
    }

    override fun simpleDialogSuccessAction() = view.showSimpleDialogAcceptedMessage()

    override fun simpleBottomSheetDialogSuccessAction() = view.showSimpleBottomSheetDialogAcceptedMessage()

    fun showSimpleDialog() = dialogNavigator.show(SimpleDialogRoute())

    fun showSimpleBottomSheetDialog() = dialogNavigator.show(SimpleBottomSheetDialogRoute())

    fun showComplexDialog() = dialogNavigator.show(ComplexDialogRoute(getSampleData()))

    fun showComplexBottomSheetDialog() = dialogNavigator.show(ComplexBottomSheetDialogRoute(getSampleData()))

    private fun getSampleData(): SampleData = SampleData(INITIAL_COMPLEX_DIALOG_VALUE)
}