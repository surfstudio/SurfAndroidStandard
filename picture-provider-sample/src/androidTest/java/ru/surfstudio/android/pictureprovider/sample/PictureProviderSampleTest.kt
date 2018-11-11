package ru.surfstudio.android.pictureprovider.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.pictureprovider.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.launchActivity

@RunWith(AndroidJUnit4::class)
@SmallTest
class PictureProviderSampleTest {

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

    @Test
    fun testPictureProviderSample() {

    }
}