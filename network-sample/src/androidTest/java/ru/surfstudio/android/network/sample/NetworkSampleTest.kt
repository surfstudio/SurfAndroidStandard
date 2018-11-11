package ru.surfstudio.android.network.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.network.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.launchActivity

@RunWith(AndroidJUnit4::class)
@SmallTest
class NetworkSampleTest {

    @Test
    fun testMvpWidgetSample() {
        launchActivity(MainActivityView::class.java)
    }
}