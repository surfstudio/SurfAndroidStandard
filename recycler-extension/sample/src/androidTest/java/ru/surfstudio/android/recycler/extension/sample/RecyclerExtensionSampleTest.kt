package ru.surfstudio.android.recycler.extension.sample

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import org.junit.Test
import ru.surfstudio.android.recycler.extension.sample.screen.*
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.RecyclerViewUtils
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkText
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class RecyclerExtensionSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testRecyclerExtensionSample() {
        test(
                buttonResId = R.id.show_sticky_recycler_btn,
                activityClass = StickyRecyclerActivity::class.java,
                lambda = {
                    checkText(STICKY_HEADER_TITLE, STICKY_FOOTER_TITLE)
                    RecyclerViewUtils.scrollToBottom(R.id.sticky_rv)
                    checkText(LAST_ITEM_TITLE)
                }
        )
        test(R.id.show_carousel_btn, CarouselActivity::class.java)
    }

    private fun <T> test(
            @IdRes buttonResId: Int,
            activityClass: Class<T>,
            lambda: () -> Unit = {}
    ) {
        performClick(buttonResId)
        checkIfActivityIsVisible(activityClass)
        lambda()
        Espresso.pressBack()
    }
}