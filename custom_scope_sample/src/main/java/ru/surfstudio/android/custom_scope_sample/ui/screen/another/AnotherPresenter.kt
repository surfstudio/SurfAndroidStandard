package ru.surfstudio.android.custom_scope_sample.ui.screen.another

import com.jakewharton.rxbinding2.InitialValueObservable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.custom_scope_sample.domain.EmailData
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class AnotherPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val emailData: EmailData
) : BasePresenter<AnotherActivityView>(basePresenterDependency) {

    private val screenModel = AnotherScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        screenModel.email = emailData.email
        view.render(screenModel)
    }

    fun observeTextChanges(textChanges: InitialValueObservable<CharSequence>) {
        subscribe(textChanges.skipInitialValue()) {
            emailData.email = it.toString()

            screenModel.email = emailData.email
        }
    }

}