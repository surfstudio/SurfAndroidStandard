package ru.surfstudio.android.core.navigation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import ru.surfstudio.android.core.navigation.sample.R
import ru.surfstudio.android.core.navigation.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.core.navigation.sample.ui.screen.profile.ProfileActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.TextUtils
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfViewIsVisible

class NavigationSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testNavigationSample() {
        onView(withId(R.id.main_profile_btn)).perform(click())
        checkIfActivityIsVisible(ProfileActivityView::class.java)
        checkIfViewIsVisible(R.id.profile_username_tv)
        TextUtils.checkViewText(R.id.profile_username_tv, R.string.user_name)
    }
}