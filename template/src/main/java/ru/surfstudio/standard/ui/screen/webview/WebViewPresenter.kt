package ru.surfstudio.standard.ui.screen.webview

import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import javax.inject.Inject

/**
 * Презентер экрана с вебвью
 */
@PerScreen
class WebViewPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                           private val route: WebViewRoute) : BasePresenter<WebViewActivityView>(basePresenterDependency) {

    private val screenModel: WebViewScreenModel

    init {
        screenModel = WebViewScreenModel(route.title)
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}