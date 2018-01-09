package ru.surfstudio.standard.ui.util.camera;

import android.net.Uri;

public interface ReviewWriteCameraCallback {
    void onPhotoUriFound(Uri photoUri);

    void onError(String message);
}