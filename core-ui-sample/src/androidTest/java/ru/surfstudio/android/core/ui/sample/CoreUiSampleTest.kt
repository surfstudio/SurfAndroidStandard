package ru.surfstudio.android.core.ui.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import ru.surfstudio.android.core.ui.sample.ui.screen.result_fragment.FRAGMENT_2_RESULT
import ru.surfstudio.android.core.ui.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkViewText
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfSnackbarIsVisible


class CoreUiSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testCoreUiSample() {
        checkIfSnackbarIsVisible(R.string.snackbar_message)
        testFragmentToActivityResult()
        testFragmentToFragmentResult()
    }

    private fun testFragmentToActivityResult() {
        performClick(R.id.button_to_fragment_2)
        onView(withId(R.id.button_back_from_fragment_2)).check(matches(isDisplayed()))
        performClick(R.id.button_back_from_fragment_2)
        VisibilityUtils.checkIfToastIsVisible(FRAGMENT_2_RESULT)
    }

    private fun testFragmentToFragmentResult() {
        performClick(R.id.button_to_fragment_2_from_fragment_1)
        onView(withId(R.id.button_back_from_fragment_2)).check(matches(isDisplayed()))
        performClick(R.id.button_back_from_fragment_2)
        checkViewText(R.id.message_from_fragment_2, FRAGMENT_2_RESULT)
    }
}