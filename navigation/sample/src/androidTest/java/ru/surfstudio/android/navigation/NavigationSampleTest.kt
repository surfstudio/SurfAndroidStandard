package ru.surfstudio.android.navigation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import ru.surfstudio.android.navigation.sample.app.screen.MainActivity
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.navigation.sample.R

class NavigationSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testNavigationSample() {
        onView(withId(R.id.splash)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.auth_btn))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.main_fragment_container))
                .check(matches(isDisplayed()))

        onView(withId(R.id.profile_tab_btn))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.profile_logout_btn))
                .check(matches(isDisplayed()))
    }
}