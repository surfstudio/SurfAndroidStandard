package ru.surfstudio.android.core.mvi.sample.ui

import androidx.annotation.CallSuper
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.After
import org.junit.Test
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.input.InputFormActivityView
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.list.ComplexListActivityView
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.main.MainActivityView
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.SimpleListActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.AnimationUtils
import ru.surfstudio.android.sample.common.test.utils.RecyclerViewUtils.performItemClick
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfSnackbarIsVisible

class MviSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

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
    fun testInputFormActivity() {
        performClick(R.id.main_open_input_form_btn)
        checkIfActivityIsVisible(InputFormActivityView::class.java)
        val testStr = "testStr"
        onView(withId(R.id.input_et)).perform(typeText(testStr), closeSoftKeyboard())
        performClick(R.id.submit_btn)
        checkIfSnackbarIsVisible(testStr)
        checkIfActivityIsVisible(MainActivityView::class.java)
    }

    @Test
    fun testInputFormActivityViewWithEmptyInput() {
        Thread.sleep(500) // ждем окончания анимации открытия активити

        performClick(R.id.main_open_input_form_btn)
        checkIfActivityIsVisible(InputFormActivityView::class.java)
        performClick(R.id.submit_btn)

        onView(withText("Try again?"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        onView(withText("ОК")).perform(click())

        performClick(R.id.submit_btn)
        onView(withText("ОТМЕНА")).perform(click())
        checkIfActivityIsVisible(MainActivityView::class.java)
    }

    @Test
    fun testSimpleListActivity() {
        performClick(R.id.main_open_simple_list_btn)
        checkIfActivityIsVisible(SimpleListActivityView::class.java)
        performItemClick(R.id.simple_list_rv, 0)
        performItemClick(R.id.simple_list_rv, 1)
        performItemClick(R.id.simple_list_rv, 2)
        pressBack()
        checkIfActivityIsVisible(MainActivityView::class.java)
    }

    @Test
    fun testComplexListActivity() {
        performClick(R.id.main_open_complex_list_btn)
        checkIfActivityIsVisible(ComplexListActivityView::class.java)
        onView(withId(R.id.reactive_query_tv)).perform(typeText("3"), closeSoftKeyboard())
        performClick(R.id.reactive_reload_btn)
        pressBack()
        checkIfActivityIsVisible(MainActivityView::class.java)
    }
}