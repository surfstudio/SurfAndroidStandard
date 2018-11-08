package ru.surfstudio.android.animations.sample

import androidx.annotation.IdRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AnimationsSampleTest {

    @Before
    fun launchActivity() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testAnimations() {
        performClick(
                R.id.show_cross_fade_animation_btn,
                R.id.reset_cross_fade_animation_btn
        )
    }

    private fun performClick(@IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it)).perform(click())
        }
    }
}