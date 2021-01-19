package ru.surfstudio.android.navigation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import ru.surfstudio.android.navigation.sample_standard.R
import ru.surfstudio.android.navigation.sample_standard.screen.splash.SplashActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest

class NavigationSampleStandardTest : BaseSampleTest<SplashActivityView>(SplashActivityView::class.java) {

    @Test
    fun testNavigationSample() {
        onView(withId(R.id.splash_layout))
                .check(matches(isDisplayed()))

        Thread.sleep(2000)

        onView(withId(R.id.guide_bottom_nav_btn))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.bottom_tabs_container))
                .check(matches(isDisplayed()))

        onView(withId(R.id.bottom_nav_fragment_container))
                .check(matches(isDisplayed()))

        onView(withId(R.id.home_add_btn))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.home_nested_add_btn))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.home_nested_add_btn))
                .check(matches(isDisplayed()))
    }
}