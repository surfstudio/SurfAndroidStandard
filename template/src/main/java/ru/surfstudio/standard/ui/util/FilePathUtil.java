package ru.surfstudio.standard.ui.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.standard.interactor.common.error.NonInstanceClassCreateException;

/**
 * утилита получения абсолютного пути по content uri
 */
public class FilePathUtil {

    private FilePathUtil() {
        throw new NonInstanceClassCreateException(FilePathUtil.class);
    }

    public static String getRealPathFromImage(Activity activity, Uri uri) {
        String realPath = null;
        if (isGooglePhotos(uri)) {
            try {
                InputStream is = activity.getContentResolver().openInputStream(uri);
                if (is != null) {
                    Bitmap pictureBitmap = BitmapFactory.decodeStream(is);
                    realPath = getRealPath(activity, getImageUri(activity, pictureBitmap));
                }
            } catch (FileNotFoundException e) {
                Logger.e(e);
            }
        } else {
            realPath = getRealPath(activity, uri);
        }
        return realPath;
    }

    private static boolean isGooglePhotos(Uri uri) {
        return uri.toString().startsWith("content://com.google.android.apps.photos.content") && uri.toString().contains("mediakey");
    }

    private static Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "img", null);
        return Uri.parse(path);
    }

    private static String getRealPath(Activity activity, Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
        return null;
    }
}
