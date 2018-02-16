package ru.surfstudio.standard.ui.screen.main

import ru.surfstudio.android.core.ui.base.message.MessageController
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.picturechooser.PictureProvider
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val photoProvider: PictureProvider,
                                                 val messageController: MessageController)
    : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun openCamera() {
        subscribeIoHandleError(photoProvider.openCameraAndTakePhoto(), { path ->
            messageController.show(path.toString())
        })
    }

    fun openGallerySingle() {
        subscribeIoHandleError(photoProvider.openGalleryAndGetPhoto(), { path ->
            messageController.show(path.toString())
        })
    }
}