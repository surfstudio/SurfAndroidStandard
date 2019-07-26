package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event.DataChangedEvent
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.event.DataChangedEventType
import ru.surfstudio.android.rxbus.RxBus
import javax.inject.Inject

@PerScreen
internal class ComplexDialogPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                          private val route: ComplexDialogRoute,
                                                          private val dialogNavigator: DialogNavigator,
                                                          private val rxBus: RxBus
) : BasePresenter<ComplexDialogFragment>(basePresenterDependency) {

    private var sm: ComplexDialogScreenModel = ComplexDialogScreenModel(route.sampleData)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun increment() {
        sm.sampleData.increment()
        view.render(sm)
    }

    fun decrement() {
        sm.sampleData.decrement()
        view.render(sm)
    }

    fun applyChanges() {
        rxBus.emitEvent(DataChangedEvent(sm.sampleData, DataChangedEventType.COMPLEX_DIALOG))
        dialogNavigator.dismiss(route)
    }
}