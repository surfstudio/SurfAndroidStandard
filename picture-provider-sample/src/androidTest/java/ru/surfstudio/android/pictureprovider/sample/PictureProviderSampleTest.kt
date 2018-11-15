package ru.surfstudio.android.pictureprovider.sample

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test
import ru.surfstudio.android.pictureprovider.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.PermissionUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class PictureProviderSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    private val pictureProviderPermissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CAMERA"
    )

    private val buttonResList = intArrayOf(
            R.id.camera_btn,
            R.id.gallery_btn,
            R.id.gallery_m_btn,
            R.id.chooser_btn,
            R.id.chooser_m_btn,
            R.id.chooser_save_btn
    )

    private val canceledResult = ActivityResult(Activity.RESULT_CANCELED, Intent())

    @Before
    override fun setUp() {
        super.setUp()
        PermissionUtils.grantPermissions(*pictureProviderPermissions)
        registerCancelResultForIntentData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        registerCancelResultForIntentData(MediaStore.ACTION_IMAGE_CAPTURE)
        registerCancelResultForIntentAction(Intent.ACTION_CHOOSER)
    }

    @Test
    fun testPictureProviderSample() {
        performClick(*buttonResList)
    }

    private fun registerCancelResultForIntentData(vararg intentUriList: Uri) {
        intentUriList.forEach {
            intending(allOf(hasData(it))).respondWith(canceledResult)
        }
    }

    private fun registerCancelResultForIntentData(vararg intentNameList: String) {
        intentNameList.forEach {
            intending(allOf(hasData(it))).respondWith(canceledResult)
        }
    }

    private fun registerCancelResultForIntentAction(vararg intentNameList: String) {
        intentNameList.forEach {
            intending(allOf(hasAction(it))).respondWith(canceledResult)
        }
    }
}