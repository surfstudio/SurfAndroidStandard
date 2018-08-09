package ru.surfstudio.android.mvp.dialog.sample.ui.screen.main

import androidx.core.widget.toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.bus.RxBus
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.ComplexDialogRoute
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.data.SampleData
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event.DataChangedEvent
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.SimpleDialogPresenter
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.SimpleDialogRoute
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom.SimpleBottomSheetDialogPresenter
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom.SimpleBottomSheetDialogRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val dialogNavigator: DialogNavigator,
                                                 private val rxBus: RxBus
) : BasePresenter<MainActivityView>(basePresenterDependency),
        SimpleDialogPresenter, SimpleBottomSheetDialogPresenter {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
        subscribe(rxBus.observeEvents(DataChangedEvent::class.java)) {
            view.toast("New value = ${it.sampleData.value}")
        }
    }

    override fun simpleDialogSuccessAction() {
        view.toast("Simple dialog accepted")
    }

    override fun simpleBottomSheetDialogSuccessAction() {
        view.toast("Simple bottom sheet dialog accepted")
    }

    fun showSimpleDialog() = dialogNavigator.show(SimpleDialogRoute())

    fun showSimpleBottomSheetDialog() = dialogNavigator.show(SimpleBottomSheetDialogRoute())

    fun showComplexDialog() = dialogNavigator.show(ComplexDialogRoute(SampleData(10)))

    fun showComplexBottomSheetDialog() {

    }
}