package ru.surfstudio.android.pictureprovider.sample.ui.screen.main

import io.reactivex.Single
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
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        stringsProvider: StringsProvider,
        private val permissionManager: PermissionManager,
        private val picturePermissionChecker: PicturePermissionChecker,
        private val photoProvider: PictureProvider,
        private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()
    private val imageChooserMessage = stringsProvider.getString(R.string.image_chooser_message)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
        subscribeIoHandleError(picturePermissionChecker.checkCameraStoragePermission()) { }
    }

    fun openCamera() = performAction(photoProvider.openCameraAndTakePhoto())

    fun openGallerySingle() = performAction(photoProvider.openGalleryAndGetPhotoUriWrapper())

    fun openGalleryMultiple() = performAction(photoProvider.openGalleryAndGetFewPhotoUriWrapper())

    fun openChooserSingle() {
        performAction(photoProvider.openImageChooserAndGetPhotoUriWrapper(imageChooserMessage))
    }

    fun openChooserMultiple() {
        performAction(photoProvider.openImageChooserAndGetFewPhotoUriWrapper(imageChooserMessage))
    }

    fun openChooserAndSavePhoto() {
        performAction(photoProvider.openImageChooserAndSavePhoto(imageChooserMessage))
    }

    private fun <T> performAction(single: Single<T>) {
        subscribeIoHandleError(single) {
            messageController.show(it.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) view.startCamera()
    }

    override fun onPause() {
        super.onPause()
        if (checkPermission()) view.stopCamera()
    }

    private fun checkPermission(): Boolean {
        return permissionManager.check(CameraStoragePermissionRequest()).isGranted
    }
}