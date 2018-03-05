package ru.surfstudio.standard.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.message.MessageController
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.picturechooser.CameraStoragePermissionRequest
import ru.surfstudio.android.picturechooser.PicturePermissionChecker
import ru.surfstudio.android.picturechooser.PictureProvider
import ru.surfstudio.standard.interactor.analytics.AnalyticsService
import ru.surfstudio.standard.interactor.analytics.event.EnterEvent
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(private val analyticsService: AnalyticsService, basePresenterDependency: BasePresenterDependency,
                                                 private val permissionManager: PermissionManager,
                                                 private val picturePermissionChecker: PicturePermissionChecker,
                                                 private val photoProvider: PictureProvider,
                                                 private val messageController: MessageController) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
        subscribeIoHandleError(picturePermissionChecker.checkCameraStoragePermission(), { _ -> })

        analyticsService.sendEvent(EnterEvent())
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

    fun openGalleryMultiple() {
        subscribeIoHandleError(photoProvider.openGalleryAndGetFewPhoto(), { path ->
            messageController.show(path.toString())
        })
    }

    override fun onResume() {
        super.onResume()
        if (permissionManager.check(CameraStoragePermissionRequest())) view.startCamera()
    }

    override fun onPause() {
        super.onPause()
        view.stopCamera()
    }
}