package ru.surfstudio.android.mvpwidget.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.mvpwidget.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.launchActivity

@RunWith(AndroidJUnit4::class)
@SmallTest
class MvpWidgetSampleTest {

    @Test
    fun testMvpWidgetSample() {
        launchActivity(MainActivityView::class.java)
    }
}