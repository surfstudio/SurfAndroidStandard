package ru.surfstudio.android.pictureprovider.sample.interactor.ui.screen.main

import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.picturechooser.CameraStoragePermissionRequest
import ru.surfstudio.android.picturechooser.PicturePermissionChecker
import ru.surfstudio.android.picturechooser.PictureProvider
import ru.surfstudio.android.pictureprovider.sample.R
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val permissionManager: PermissionManager,
                                                 private val picturePermissionChecker: PicturePermissionChecker,
                                                 private val photoProvider: PictureProvider,
                                                 private val stringsProvider: StringsProvider,
                                                 private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
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
        subscribeIoHandleError(photoProvider.openImageChooserAndGetPhotoUriWrapper(getImageChooserMessage())) { uriWrapper ->
            messageController.show(uriWrapper.uri.toString())
        }
    }

    fun openChooserMultiple() {
        subscribeIoHandleError(photoProvider.openImageChooserAndGetFewPhotoUriWrapper(getImageChooserMessage())) { uriWrapperList ->
            messageController.show(uriWrapperList.toString())
        }
    }

    fun openChooserAndSavePhoto() {
        subscribeIoHandleError(photoProvider.openImageChooserAndSavePhoto(getImageChooserMessage())) { path ->
            messageController.show(path)
        }
    }

    private fun getImageChooserMessage(): String = stringsProvider.getString(R.string.image_chooser_message)

    override fun onResume() {
        super.onResume()
        if (permissionManager.check(CameraStoragePermissionRequest()).isGranted) view.startCamera()
    }

    override fun onPause() {
        super.onPause()
        view.stopCamera()
    }
}