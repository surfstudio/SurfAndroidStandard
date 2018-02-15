package com.example.camera

import android.net.Uri
import com.example.camera.exceptions.NoPermissionException
import com.example.camera.exceptions.PhotoException
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.logger.Logger
import ru.trinitydigital.poison.ui.util.camera.CameraIntentHelper
import ru.trinitydigital.poison.ui.util.camera.CameraIntentHelperCallback
import java.util.*
import javax.inject.Inject


/**
 * Created by vsokolova on 2/8/18.
 */
@PerActivity
class PhotoProvider @Inject constructor(
        private val activityProvider: ActivityProvider,
        private val cameraStoragePermissionChecker: CameraStoragePermissionChecker) {


    private val takePhotoPublishSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private val getPhotoPublishSubject: PublishSubject<String> = PublishSubject.create()
    private val previewPhotoPublishSubject: PublishSubject<List<String>> = PublishSubject.create()

    val cameraIntentHelper = CameraIntentHelper(activityProvider.get(), object : CameraIntentHelperCallback {
        override fun deletePhotoWithUri(photoUri: Uri) {
            takePhotoPublishSubject.onError(PhotoException())
        }

        override fun onSdCardNotMounted() {
            takePhotoPublishSubject.onError(PhotoException())
        }

        override fun onCanceled() {
            takePhotoPublishSubject.onError(PhotoException())
        }

        override fun onCouldNotTakePhoto() {
            takePhotoPublishSubject.onError(PhotoException())
        }

        override fun onPhotoUriNotFound() {
            takePhotoPublishSubject.onError(PhotoException())
        }

        override fun logException(e: Exception) {
            Logger.e("Photo Exception", e)
        }

        override fun onPhotoUriFound(dateCameraIntentStarted: Date, photoUri: Uri?, rotateXDegrees: Int) {
            if (photoUri != null) {
                takePhotoPublishSubject.onNext(photoUri.path)
            } else {
                takePhotoPublishSubject.onError(PhotoException())
            }
        }
    })

    fun openCameraAndTakePhoto(ifNoPermissionAction: () -> Unit = {}): Observable<String> {
        cameraStoragePermissionChecker.checkCameraStoragePermission().subscribe({ hasPermission ->
            if (hasPermission) {
                cameraIntentHelper.startCameraIntent()
            } else {
                takePhotoPublishSubject.onError(NoPermissionException())
            }
        })

        return takePhotoPublishSubject
    }

//    fun openGalleryAndGetPhoto(ifNoPermissionAction: () -> Unit = {}): Observable<String> {
//        return ""
//    }
//
//    fun getPhotosForPreview(ifNoPermissionAction: () -> Unit = {}): Observable<List<String>> {
//        return emptyList()
//    }
}