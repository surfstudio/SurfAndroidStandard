package ru.surfstudio.android.security.sample.ui.screen.pin

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.StringsProvider
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
        loadData(profileInteractor.signIn(route.apiKey, pin)) {
            val message = stringsProvider.getString(R.string.pin_created_message)
            view.showMessage(message)
        }
    }

    fun getApiKey(pin: String) {
        loadData(profileInteractor.getApiKey(pin)) {
            view.showMessage(it.apiKey)
        }
    }

    private fun <T> loadData(observable: Observable<T>,
                             onNext: (T) -> Unit) {
        subscribeIoHandleError(observable, {
            onNext(it)
            view.render(screenModel)
        }, {
            view.render(screenModel)
        })
    }
}
