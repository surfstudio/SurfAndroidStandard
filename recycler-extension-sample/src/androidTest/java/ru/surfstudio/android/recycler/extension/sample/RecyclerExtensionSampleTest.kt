package ru.surfstudio.android.recycler.extension.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.checkText
import ru.surfstudio.android.sample.common.test.utils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.scrollTo

@RunWith(AndroidJUnit4::class)
@SmallTest
class RecyclerExtensionSampleTest {

    @Before
    fun setUp() {
        launchActivity(MainActivity::class.java)
    }

    @Test
    fun testPictureProviderSample() {
        checkText(STICKY_HEADER_TITLE, STICKY_FOOTER_TITLE)
        scrollTo(R.id.activity_main_recycler, LAST_ITEM_TITLE)
        checkText(LAST_ITEM_TITLE)
    }
}