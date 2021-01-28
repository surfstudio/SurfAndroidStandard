package ru.surfstudio.android.permission

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import ru.surfstudio.android.permission.sample.R
import ru.surfstudio.android.permission.sample.screen.main.MainActivityView
import ru.surfstudio.android.permission.sample.screen.main.camera.CameraPermissionRequest
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.PermissionUtils

class PermissionSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testPermissionRequest() {
        PermissionUtils.grantPermissions(CameraPermissionRequest().permissions.first())
        onView(withId(R.id.go_to_camera_btn)).perform(click())
    }
}