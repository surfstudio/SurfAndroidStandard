package ru.surfstudio.standard.ui.util.camera;


import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import rx.Observable;

/**
 * Сервис для получения картинок из галереи для экрана создания рецензии
 */
@PerScreen
public class MediaStoreService {

    private ActivityProvider activityProvider;

    @Inject
    public MediaStoreService(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    /**
     * Получить отсортированные пути к картинкам из галлереи
     */
    public Observable<ArrayList<String>> getAllStorageImagesPath() {
        return Observable.create(subscriber -> {
            ArrayList<String> images = new ArrayList<>();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT};
            Cursor cursor = activityProvider.get().getContentResolver().query(uri, projection, null, null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC");
            if (cursor != null) {
                int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                int columnIndexWidth = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH);
                int columnIndexHeight= cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT);
                while (cursor.moveToNext()) {
                    int width = cursor.getInt(columnIndexWidth);
                    int height = cursor.getInt(columnIndexHeight);
                    //не добавляем некорректные изображения
                    if (width > 0 && height > 0) {
                        String absolutePathOfImage = cursor.getString(columnIndexData);
                        images.add(absolutePathOfImage);
                    }
                }
                cursor.close();
            }
            subscriber.onNext(images);
            subscriber.onCompleted();
        });
    }
}