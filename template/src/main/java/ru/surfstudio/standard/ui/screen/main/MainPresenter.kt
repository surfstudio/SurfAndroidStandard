package ru.surfstudio.standard.ui.screen.main

import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.app.log.Logger
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 val photoProvider: PhotoProvider) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun openCamera() {
        subscribe(photoProvider.openCameraAndTakePhoto(), { path ->
            Logger.d("Path" + path)
        }, { e ->
            Logger.d("Path Error" + e)
        })
    }
}