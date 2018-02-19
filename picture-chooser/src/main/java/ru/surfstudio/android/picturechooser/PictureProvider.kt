package ru.surfstudio.android.picturechooser

import io.reactivex.Observable
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.picturechooser.exceptions.NoPermissionException
import javax.inject.Inject


/**
 * Поставляет изображения находящиеся на устройстве или с камеры.
 */
@PerActivity
class PictureProvider @Inject constructor(
        activityNavigator: ActivityNavigator,
        activityProvider: ActivityProvider,
        private val cameraStoragePermissionChecker: PicturePermissionChecker) {

    private val cameraIntentHelper = CameraPictureProvider(activityNavigator, activityProvider.get())
    private val galleryPictureProvider = GalleryPictureProvider(activityNavigator, activityProvider.get())

    /**
     *  Запускает сторонее приложение камеры для получения изображение.
     *  @return Observable Uri изображения и угол поворота.
     */
    fun openCameraAndTakePhoto(noPermissionAction: () -> Unit = {}): Observable<CameraPictureProvider.CameraResult> {
        return cameraStoragePermissionChecker.checkCameraStoragePermission()
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        cameraIntentHelper.startCameraIntent()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }

    /**
     *  Запускает сторонее приложение галереи для получения изображение.
     *  @return Observable Uri изображения.
     */
    fun openGalleryAndGetPhoto(noPermissionAction: () -> Unit = {}): Observable<String> {
        return cameraStoragePermissionChecker.checkGalleryStoragePermission()
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        galleryPictureProvider.openGalleryForSingleImage()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }

    /**
     *  Запускает сторонее приложение галереи для получения изображение.
     *  @return Observable Uri изображения.
     */
    fun openGalleryAndGetFewPhoto(noPermissionAction: () -> Unit = {}): Observable<List<String>> {
        return cameraStoragePermissionChecker.checkGalleryStoragePermission()
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        galleryPictureProvider.openGalleryForMultipleImage()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }
}