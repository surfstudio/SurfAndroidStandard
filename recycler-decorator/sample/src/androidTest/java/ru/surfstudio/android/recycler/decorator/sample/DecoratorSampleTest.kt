package ru.surfstudio.android.recycler.decorator.sample

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Test
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.AnimationUtils
import ru.surfstudio.android.sample.common.test.utils.RecyclerViewUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class DecoratorSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    override fun setUp() {
        super.setUp()
        AnimationUtils.grantScaleAnimationPermission()
        AnimationUtils.disableAnimations()
    }

    @After
    @CallSuper
    fun tearDown() {
        AnimationUtils.enableAnimations()
    }

    @Test
    fun testChatSample() {
        performClick(R.id.chat_btn)
        RecyclerViewUtils.scrollToBottom(R.id.recycler_view)
    }

    @Test
    fun testEasySimpleSample() {
        performClick(R.id.easy_adapter_decor_btn)
        RecyclerViewUtils.scrollToBottom(R.id.recycler_view)
    }

    @Test
    fun testLinearSample() {
        performClick(R.id.linear_decor_btn)
        RecyclerViewUtils.scrollToBottom(R.id.recycler_view)
    }

    @Test
    fun testPagerSample() {
        performClick(R.id.pager_decor_btn)
        RecyclerViewUtils.scrollToBottom(R.id.pager_rv)
    }
}