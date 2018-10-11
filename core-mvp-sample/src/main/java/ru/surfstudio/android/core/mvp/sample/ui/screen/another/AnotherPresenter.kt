package ru.surfstudio.android.core.mvp.sample.ui.screen.another

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.sample.ui.core.CustomOnDestroyDelegate
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class AnotherPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                    @Suppress("UNUSED_PARAMETER") customOnDestroyDelegate: CustomOnDestroyDelegate
) : BasePresenter<AnotherActivityView>(basePresenterDependency) {

    private val screenModel = AnotherScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}