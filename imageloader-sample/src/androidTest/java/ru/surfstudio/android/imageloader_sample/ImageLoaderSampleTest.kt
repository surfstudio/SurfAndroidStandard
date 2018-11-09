package ru.surfstudio.android.imageloader_sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.performClick

@RunWith(AndroidJUnit4::class)
@SmallTest
class ImageLoaderSampleTest {

    @Test
    fun testImageLoaderSample() {
        launchActivity(MainActivity::class.java)
        performClick(R.id.image_loader_sample_btn)
    }
}