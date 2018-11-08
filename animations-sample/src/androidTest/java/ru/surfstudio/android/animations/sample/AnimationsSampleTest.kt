package ru.surfstudio.android.animations.sample

import androidx.annotation.IdRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AnimationsSampleTest {

    private val widgetAnimationButtons = arrayOf(R.id.show_animation_btn, R.id.reset_animation_btn)

    @Before
    fun launchActivity() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testAnimations() {
        // test cross fade animation
        performClick(R.id.show_cross_fade_animation_btn, R.id.reset_cross_fade_animation_btn)

        // test fade-in and fade-out animation
        performClickOnWidget(R.id.fade_animation_widget)

        // test pulse animations
        performClickOnWidget(R.id.pulse_animation_widget)

        // test animation of size changing
        performClickOnWidget(R.id.new_size_animation_widget)

        //scroll to bottom
        onView(withId(R.id.slide_animation_widget)).perform(nestedScrollTo())

        // test slide animations
        performClickOnWidget(R.id.slide_animation_widget)
    }

    @Test
    fun testButtonClick() {
        performClick(R.id.bottom_btn)
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(isDisplayed()))
    }

    private fun performClick(@IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it)).perform(click())
        }
    }

    private fun performClickOnWidget(@IdRes widgetResId: Int) {
        widgetAnimationButtons.forEach {
            onView(allOf(
                    withId(it),
                    withParent(withId(widgetResId)))
            ).perform(click())
        }
    }
}