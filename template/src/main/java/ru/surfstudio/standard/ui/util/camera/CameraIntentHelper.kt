package ru.surfstudio.standard.ui.util.camera


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import ru.surfstudio.android.logger.Logger
import java.io.File
import java.util.*


/**
 * Хелпер для открытия экрана камеры
 * https://github.com/ralfgehrer/AndroidCameraUtil
 *
 *
 * //todo после тестирования на всех девайсах отрефакторить, и комментарии на русские перевести, или заменить на другое решение
 */

class CameraIntentHelper(private val activity: Activity, cameraIntentHelperCallback: ReviewWriteCameraCallback) {

    /**
     * Date and time the camera intent was started.
     */
    private var dateCameraIntentStarted: Date? = null
    /**
     * Default location where we want the photo to be ideally stored.
     */
    private var preDefinedCameraUri: Uri? = null
    /**
     * Retrieved location of the photo.
     */
    private var photoUri: Uri? = null
    /**
     * Potential 3rd location of photo beacon.
     */
    private var photoUriIn3rdLocation: Uri? = null
    /**
     * Orientation of the retrieved photo.
     */
    private var rotateXDegrees = 0

    private val cameraIntentHelperCallback: CameraIntentHelperCallback?


    init {
        this.cameraIntentHelperCallback = object : CameraIntentHelperCallback {
            override fun onPhotoUriFound(dateCameraIntentStarted: Date?, photoUri: Uri?, rotateXDegrees: Int) {
                if (photoUri != null) {
                    cameraIntentHelperCallback.onPhotoUriFound(photoUri)
                }
            }

            override fun deletePhotoWithUri(photoUri: Uri) {

            }

            override fun onSdCardNotMounted() {
                cameraIntentHelperCallback.onError("")
            }

            override fun onCanceled() {

            }

            override fun onCouldNotTakePhoto() {
                cameraIntentHelperCallback.onError("")
            }

            override fun onPhotoUriNotFound() {
                cameraIntentHelperCallback.onError("")
            }

            override fun logException(e: Exception) {

            }
        }
    }

    /**
     * Starts the camera intent depending on the device configuration.
     *
     *
     * **for Samsung and Sony devices:**
     * We call the camera activity with the method call to startActivityForResult. We only set the
     * constant CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE. We do NOT set any other intent extras.
     *
     *
     * **for all other devices:**
     * We call the camera activity with the method call to startActivityForResult as previously.
     * This time, however, we additionally set the intent extra MediaStore.EXTRA_OUTPUT and provide
     * an URI, where we want the image to be stored.
     *
     *
     * In both cases we remember the time the camera activity was started.
     */
    fun startCameraIntent() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            try {
                // NOTE: Do NOT SET: intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPicUri)
                // on Samsung Galaxy S2/S3/.. for the following reasons:
                // 1.) it will break the correct picture orientation
                // 2.) the photo will be stored in two locations (the given path and, additionally, in the MediaStore)
                val manufacturer = android.os.Build.MANUFACTURER.toLowerCase(Locale.ENGLISH)
                val model = android.os.Build.MODEL.toLowerCase(Locale.ENGLISH)
                val buildType = android.os.Build.TYPE.toLowerCase(Locale.ENGLISH)
                val buildDevice = android.os.Build.DEVICE.toLowerCase(Locale.ENGLISH)
                val buildId = android.os.Build.ID.toLowerCase(Locale.ENGLISH)
                //				String sdkVersion = android.os.Build.VERSION.RELEASE.toLowerCase(Locale.ENGLISH);

                Logger.e("Camera manufacturer name = " + manufacturer)

                var setPreDefinedCameraUri = true
                if (manufacturer.contains("samsung")) {
                    setPreDefinedCameraUri = true
                }
                if (!manufacturer.contains("samsung") && !manufacturer.contains("sony")) {
                    setPreDefinedCameraUri = true
                }
                if (manufacturer.contains("samsung") && model.contains("galaxy nexus")) { //TESTED
                    setPreDefinedCameraUri = true
                }
                if (manufacturer.contains("samsung") && model.contains("gt-n7000") && buildId.contains("imm76l")) { //TESTED
                    setPreDefinedCameraUri = true
                }

                if (buildType.contains("userdebug") && buildDevice.contains("ariesve")) {  //TESTED
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("crespo")) {   //TESTED
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("gt-i9100")) { //TESTED
                    setPreDefinedCameraUri = true
                }

                ///////////////////////////////////////////////////////////////////////////
                // TEST
                if (manufacturer.contains("samsung") && model.contains("sgh-t999l")) { //T-Mobile LTE enabled Samsung S3
                    setPreDefinedCameraUri = true
                }
                if (buildDevice.contains("cooper")) {
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("t0lte")) {
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("kot49h")) {
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("t03g")) {
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("gt-i9300")) {
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("gt-i9195")) {
                    setPreDefinedCameraUri = true
                }
                if (buildType.contains("userdebug") && buildDevice.contains("xperia u")) {
                    setPreDefinedCameraUri = true
                }

                ///////////////////////////////////////////////////////////////////////////


                dateCameraIntentStarted = Date()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (setPreDefinedCameraUri) {
                    val filename = System.currentTimeMillis().toString() + ".jpg"
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, filename)
                    preDefinedCameraUri = activity.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, preDefinedCameraUri)
                }
                activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                if (cameraIntentHelperCallback != null) {
                    cameraIntentHelperCallback.logException(e)
                    cameraIntentHelperCallback.onCouldNotTakePhoto()
                }
            }

        } else {
            onSdCardNotMounted()
        }
    }

    /**
     * Receives all activity results and triggers onCameraIntentResult if
     * the requestCode matches.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            onCameraIntentResult(resultCode, intent)
        }
    }


    /**
     * On camera activity result, we try to locate the photo.
     *
     *
     * **Mediastore:**
     * First, we try to read the photo being captured from the MediaStore. Using a ContentResolver
     * on the MediaStore content, we retrieve the latest image being taken, as well as its
     * orientation property and its timestamp. If we find an imageH and it was not taken before
     * the camera intent was called, it is the image we were looking for. Otherwise, we dismiss
     * the result and try one of the following approaches.
     *
     *
     * **Intent extra:**
     * Second, we try to getScreenDelegateFactory an image Uri from intent.getBeacon() of the returning intent.
     * If this is not successful either, we continue with step 3.
     *
     *
     * **Default photo Uri:**
     * If all of the above mentioned steps did not work, we use the image Uri we passed to
     * the camera activity.
     *
     * @param resultCode
     * @param intent
     */
    private fun onCameraIntentResult(resultCode: Int, intent: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            var myCursor: Cursor? = null
            var dateOfPicture: Date? = null
            try {
                // Create a Cursor to obtain the file Path for the large image
                val largeFileProjection = arrayOf(MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.ORIENTATION, MediaStore.Images.ImageColumns.DATE_TAKEN)
                val largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC"
                myCursor = activity.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        largeFileProjection, null, null,
                        largeFileSort)
                myCursor!!.moveToFirst()
                if (!myCursor.isAfterLast) {
                    // This will actually give you the file path location of the image.
                    val largeImagePath = myCursor.getString(myCursor
                            .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                    photoUri = Uri.fromFile(File(largeImagePath))
                    if (photoUri != null) {
                        dateOfPicture = Date(myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)))
                        if (dateOfPicture != null && dateOfPicture.after(dateCameraIntentStarted)) {
                            rotateXDegrees = myCursor.getInt(myCursor
                                    .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION))
                        } else {
                            photoUri = null
                        }
                    }
                    if (myCursor.moveToNext() && !myCursor.isAfterLast) {
                        val largeImagePath3rdLocation = myCursor.getString(myCursor
                                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                        val dateOfPicture3rdLocation = Date(myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)))
                        if (dateOfPicture3rdLocation != null && dateOfPicture3rdLocation.after(dateCameraIntentStarted)) {
                            photoUriIn3rdLocation = Uri.fromFile(File(largeImagePath3rdLocation))
                        }
                    }
                }
            } catch (e: Exception) {
                cameraIntentHelperCallback?.logException(e)
            } finally {
                if (myCursor != null && !myCursor.isClosed) {
                    myCursor.close()
                }
            }

            if (photoUri == null) {
                try {
                    photoUri = intent.data
                } catch (e: Exception) {
                    Logger.e(e)
                }
            }

            if (photoUri == null) {
                photoUri = preDefinedCameraUri
            }

            try {
                if (photoUri != null && File(photoUri!!.path).length() <= 0) {
                    if (preDefinedCameraUri != null) {
                        val tempUri = photoUri
                        photoUri = preDefinedCameraUri
                        preDefinedCameraUri = tempUri
                    }
                }
            } catch (e: Exception) {
                Logger.e(e)
            }

            photoUri = getFileUriFromContentUri(photoUri)
            preDefinedCameraUri = getFileUriFromContentUri(preDefinedCameraUri)
            try {
                if (photoUriIn3rdLocation != null) {
                    if (photoUriIn3rdLocation == photoUri || photoUriIn3rdLocation == preDefinedCameraUri) {
                        photoUriIn3rdLocation = null
                    } else {
                        photoUriIn3rdLocation = getFileUriFromContentUri(photoUriIn3rdLocation)
                    }
                }
            } catch (e: Exception) {
                Logger.e(e)
            }

            if (photoUri != null) {
                onPhotoUriFound()
            } else {
                onPhotoUriNotFound()
            }
            deletePhotoInOtherLocations()
        } else if (resultCode == Activity.RESULT_CANCELED) {
            cameraIntentHelperCallback?.onCanceled()
        } else {
            cameraIntentHelperCallback?.onCanceled()
        }
    }

    /**
     * On some devices the taken photo is being stored in multiple locations. Consequently, we have
     * to remove images in any other location but the the one image in photoUri.
     */
    private fun deletePhotoInOtherLocations() {
        if (preDefinedCameraUri != photoUri && cameraIntentHelperCallback != null) {
            preDefinedCameraUri?.let(cameraIntentHelperCallback::deletePhotoWithUri)
        }
        if (photoUriIn3rdLocation != photoUri && cameraIntentHelperCallback != null) {
            photoUriIn3rdLocation?.let(cameraIntentHelperCallback::deletePhotoWithUri)
        }
    }

    /**
     * Being called if the photo could be located. The photo's Uri
     * and its orientation could be retrieved.
     */
    private fun onPhotoUriFound() {
        cameraIntentHelperCallback?.onPhotoUriFound(dateCameraIntentStarted, photoUri, rotateXDegrees)
    }

    /**
     * Being called if the photo could not be located (photoUri == null).
     */
    private fun onPhotoUriNotFound() {
        cameraIntentHelperCallback?.onPhotoUriNotFound()
    }

    /**
     * Being called if the SD card (or the internal mass storage respectively) is not mounted.
     */
    private fun onSdCardNotMounted() {
        cameraIntentHelperCallback?.onSdCardNotMounted()
    }

    /**
     * Given an Uri that is a content Uri
     * (e.g.content://media/external/images/media/1884) this function returns the
     * respective file Uri, that is e.g. file://media/external/DCIM/abc.jpg
     *
     * @param cameraPicUri
     * @return Uri
     */
    private fun getFileUriFromContentUri(cameraPicUri: Uri?): Uri? {
        var cursor: Cursor? = null
        try {
            if (cameraPicUri != null && cameraPicUri.toString().startsWith("content")) {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = activity.contentResolver.query(cameraPicUri, proj, null, null, null)
                cursor!!.moveToFirst()
                // This will actually give you the file path location of the image.
                val largeImagePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                return Uri.fromFile(File(largeImagePath))
            }
            return cameraPicUri
        } catch (e: Exception) {
            return cameraPicUri
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
    }

    companion object {

        val TAKE_PHOTO_CODE = 612

        private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = TAKE_PHOTO_CODE
    }
}
