package ru.surfstudio.android.custom_view_sample

import androidx.annotation.IdRes
import org.junit.Test
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfViewIsNotVisible
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfViewIsVisible

class CustomViewSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    @IdRes
    private val placeholderIdRes = R.id.placeholder_view

    @Test
    fun testCustomViewSample() {
        checkIfViewIsVisible(R.id.progress_bar_container, placeholderIdRes)
        checkIfViewIsNotVisible(R.id.placeholder_content_container, placeholderIdRes)
        performClick(R.id.change_state_btn)
    }
}