package ru.surfstudio.android.mvpwidget.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.mvpwidget.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.launchActivity

@RunWith(AndroidJUnit4::class)
@SmallTest
class MvpWidgetSampleTest {

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

    @Test
    fun testMvpWidgetSample() {

    }
}