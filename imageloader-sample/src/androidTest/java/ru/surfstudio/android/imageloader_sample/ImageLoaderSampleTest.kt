package ru.surfstudio.android.imageloader_sample

import org.junit.Test
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class ImageLoaderSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testImageLoaderSample() {
        performClick(R.id.image_loader_sample_btn)
    }
}