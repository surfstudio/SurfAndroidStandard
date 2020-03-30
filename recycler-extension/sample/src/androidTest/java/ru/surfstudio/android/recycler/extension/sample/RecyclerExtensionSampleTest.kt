package ru.surfstudio.android.recycler.extension.sample

import org.junit.Test
import ru.surfstudio.android.recycler.extension.sample.screen.LAST_ITEM_TITLE
import ru.surfstudio.android.recycler.extension.sample.screen.MainActivity
import ru.surfstudio.android.recycler.extension.sample.screen.STICKY_FOOTER_TITLE
import ru.surfstudio.android.recycler.extension.sample.screen.STICKY_HEADER_TITLE
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.RecyclerViewUtils
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkText

class RecyclerExtensionSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testRecyclerExtensionSample() {
        checkText(STICKY_HEADER_TITLE, STICKY_FOOTER_TITLE)
        RecyclerViewUtils.scrollTo(R.id.activity_sticky_recycler, LAST_ITEM_TITLE)
        checkText(LAST_ITEM_TITLE)
    }
}