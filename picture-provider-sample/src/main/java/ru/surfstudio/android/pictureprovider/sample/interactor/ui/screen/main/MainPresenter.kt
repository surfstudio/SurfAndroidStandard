package ru.surfstudio.android.pictureprovider.sample.interactor.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.picturechooser.CameraStoragePermissionRequest
import ru.surfstudio.android.picturechooser.PicturePermissionChecker
import ru.surfstudio.android.picturechooser.PictureProvider
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val permissionManager: PermissionManager,
                                                 private val picturePermissionChecker: PicturePermissionChecker,
                                                 private val photoProvider: PictureProvider,
                                                 private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
        subscribeIoHandleError(picturePermissionChecker.checkCameraStoragePermission()) { _ -> }
    }

    fun openCamera() {
        subscribeIoHandleError(photoProvider.openCameraAndTakePhoto()) { path ->
            messageController.show(path.toString())
        }
    }

    fun openGallerySingle() {
        subscribeIoHandleError(photoProvider.openGalleryAndGetPhotoUriWrapper()) { uriWrapper ->
            messageController.show(uriWrapper.uri.toString())
        }
    }

    fun openGalleryMultiple() {
        subscribeIoHandleError(photoProvider.openGalleryAndGetFewPhotoUriWrapper()) { uriWrapperList ->
            messageController.show(uriWrapperList.toString())
        }
    }

    fun openChooserSingle() {
        subscribeIoHandleError(photoProvider.openImageChooserAndGetPhotoUriWrapper(view.getImageChooserMessage())) { uriWrapper ->
            messageController.show(uriWrapper.uri.toString())
        }
    }

    fun openChooserMultiple() {
        subscribeIoHandleError(photoProvider.openImageChooserAndGetFewPhotoUriWrapper(view.getImageChooserMessage())) { uriWrapperList ->
            messageController.show(uriWrapperList.toString())
        }
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