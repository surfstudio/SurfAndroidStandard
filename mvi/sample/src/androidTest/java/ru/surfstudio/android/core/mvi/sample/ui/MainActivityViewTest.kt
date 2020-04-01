package ru.surfstudio.android.core.mvi.sample.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormActivityView
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ComplexListActivityView
import ru.surfstudio.android.core.mvi.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list.SimpleListActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.RecyclerViewUtils.performItemClick
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfSnackbarIsVisible


class MainActivityViewTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testMviSample() {
        performClick(R.id.main_open_input_form_btn)
        checkIfActivityIsVisible(InputFormActivityView::class.java)
        val testStr = "testStr"
        onView(withId(R.id.input_et)).perform(typeText(testStr), closeSoftKeyboard())
        performClick(R.id.submit_btn)
        checkIfSnackbarIsVisible(testStr)

        performClick(R.id.main_open_simple_list_btn)
        checkIfActivityIsVisible(SimpleListActivityView::class.java)
        performItemClick(R.id.simple_list_rv, 0)
        performItemClick(R.id.simple_list_rv, 1)
        performItemClick(R.id.simple_list_rv, 2)
        pressBack()

        performClick(R.id.main_open_complex_list_btn)
        checkIfActivityIsVisible(ComplexListActivityView::class.java)
        onView(withId(R.id.reactive_query_tv)).perform(typeText("3"), closeSoftKeyboard())
        performClick(R.id.reactive_reload_btn)
        pressBack()
    }
}