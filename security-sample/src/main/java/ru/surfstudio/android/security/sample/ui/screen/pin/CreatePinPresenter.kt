package ru.surfstudio.android.security.sample.ui.screen.pin

import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.interactor.profile.ProfileInteractor
import javax.inject.Inject

/**
 * Презентер экрана ввода pin-кода
 */
@PerScreen
class CreatePinPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                             private val route: CreatePinActivityRoute,
                                             private val stringsProvider: StringsProvider,
                                             private val profileInteractor: ProfileInteractor
) : BasePresenter<CreatePinActivityView>(basePresenterDependency) {

    private val screenModel = CreatePinScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun submitPin(pin: String) {
        subscribeIoHandleError(profileInteractor.signIn(route.apiKey, pin)) {
            val message = stringsProvider.getString(R.string.pin_created_message)
            view.showMessage(message)
            view.render(screenModel)
        }
    }

    fun getApiKey(pin: String) {
        subscribeIoHandleError(profileInteractor.getApiKey(pin)) {
            view.showMessage(it.apiKey)
            view.render(screenModel)
        }
    }
}
