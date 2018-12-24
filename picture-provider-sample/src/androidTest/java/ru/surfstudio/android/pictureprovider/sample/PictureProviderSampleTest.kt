package ru.surfstudio.android.pictureprovider.sample

import android.Manifest
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.pictureprovider.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

@RunWith(AndroidJUnit4::class)
class PictureProviderSampleTest {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
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
    fun setUp() {
        Intents.init()
        registerCancelResultForIntentData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        registerCancelResultForIntentData(MediaStore.ACTION_IMAGE_CAPTURE)
        registerCancelResultForIntentAction(Intent.ACTION_CHOOSER)
        ActivityUtils.launchActivity(MainActivityView::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
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