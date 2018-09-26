package ru.surfstudio.android.custom_scope_sample.ui.screen.other

import com.jakewharton.rxbinding2.InitialValueObservable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.custom_scope_sample.domain.EmailData
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class OtherPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<OtherActivityView>(basePresenterDependency) {

    private val screenModel = OtherScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun observeTextChanges(textChanges: InitialValueObservable<CharSequence>) {
        subscribe(textChanges.skipInitialValue()) {
        }
    }

}