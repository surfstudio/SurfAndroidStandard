package ru.surfstudio.android.shared.pref.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.launchActivity
import ru.surfstudio.android.shared.pref.sample.ui.screen.main.MainActivityView

@RunWith(AndroidJUnit4::class)
@SmallTest
class SharedPrefSampleTest {

    @Test
    fun testSharedPrefSample() {
        launchActivity(MainActivityView::class.java)
    }
}