package ru.surfstudio.standard.ui.screen.main

import com.example.standarddialog.StandardDialogPresenter
import com.example.standarddialog.StandardDialogRoute
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.standard.R
import ru.surfstudio.standard.interactor.analytics.AnalyticsService
import ru.surfstudio.standard.interactor.analytics.event.EnterEvent
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val analyticsService: AnalyticsService,
                                                 private val dialogNavigator: DialogNavigator) : BasePresenter<MainActivityView>(basePresenterDependency), StandardDialogPresenter {

    override fun positiveBtnAction(dialogTag: String) {
        if (dialogTag == "tagSimple") view.toast("ok")
    }

    override fun negativeBtnAction(dialogTag: String) {
        if (dialogTag == "tagSimple") view.toast("cancel")
    }

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)

        analyticsService.sendEvent(EnterEvent())
    }

    fun OnBtnClick() {
        dialogNavigator.show(StandardDialogRoute(view,
                R.string.title,
                R.string.message,
                R.string.possitive,
                R.string.negative,
                true,
                "tagSimple"))
    }
}