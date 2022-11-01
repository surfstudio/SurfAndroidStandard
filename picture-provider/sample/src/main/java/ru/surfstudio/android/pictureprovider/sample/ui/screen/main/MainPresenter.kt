package ru.surfstudio.android.pictureprovider.sample.ui.screen.main

import android.content.Context
import io.reactivex.Single
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.picturechooser.CameraRouteFactory
import ru.surfstudio.android.picturechooser.PicturePermissionChecker
import ru.surfstudio.android.picturechooser.PictureProvider
import ru.surfstudio.android.picturechooser.destination.ContentResolverUriProvider
import ru.surfstudio.android.pictureprovider.sample.R
import javax.inject.Inject
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        resourceProvider: ResourceProvider,
        private val picturePermissionChecker: PicturePermissionChecker,
        private val photoProvider: PictureProvider,
        private val messageController: MessageController,
        private val context: Context
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()
    private val chooserTitle = resourceProvider.getString(R.string.image_chooser_message)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
        subscribeIoHandleError(picturePermissionChecker.checkCameraStoragePermission()) { }
    }

    fun openCamera() = performAction(photoProvider.openCameraAndTakePhoto())

    fun openCameraUri() = subscribeIoHandleError(
            photoProvider.openCameraAndTakePhotoUri(
                    destinationProvider = ContentResolverUriProvider(contentResolver = context.contentResolver),
                    cameraRouteFactory = CameraRouteFactory(chooserTitle = chooserTitle)
            )
    ) {
        val uri = it.uri
        view.render(sm.apply { this.photoUri = uri })
    }

    fun openGallerySingle() = performAction(photoProvider.openGalleryAndGetPhotoUriWrapper())

    fun openGalleryMultiple() = performAction(photoProvider.openGalleryAndGetFewPhotoUriWrapper())

    fun openChooserSingle() {
        performAction(photoProvider.openImageChooserAndGetPhotoUriWrapper(chooserTitle))
    }

    fun openChooserMultiple() {
        performAction(photoProvider.openImageChooserAndGetFewPhotoUriWrapper(chooserTitle))
    }

    fun openChooserAndSavePhoto() {
        performAction(photoProvider.openImageChooserAndSavePhoto(chooserTitle))
    }

    private fun <T> performAction(single: Single<T>) {
        subscribeIoHandleError(single) {
            messageController.show(it.toString())
        }
    }
}