package ru.surfstudio.standard.ui.util.camera;

import android.net.Uri;

import java.util.Date;

/**
 * Specifies the interface of a CameraIntentHelper request. The calling class has to implement the
 * interface in order to be notified when the request completes, either successfully or with an error.
 *
 * @author Ralf Gehrer <ralf@ecotastic.de>
 */
public interface CameraIntentHelperCallback {
    void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees);

    void deletePhotoWithUri(Uri photoUri);

    void onSdCardNotMounted();

    void onCanceled();

    void onCouldNotTakePhoto();

    void onPhotoUriNotFound();

    void logException(Exception e);
}