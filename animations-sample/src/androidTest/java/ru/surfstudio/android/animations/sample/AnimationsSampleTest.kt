package ru.surfstudio.android.animations.sample

import androidx.annotation.IdRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.nestedScrollTo
import ru.surfstudio.android.sample.common.test.utils.performClick

@RunWith(AndroidJUnit4::class)
@SmallTest
class AnimationsSampleTest {

    // Массив id кнопок виджета для показа и скрытия анимации
    private val widgetAnimationButtons = arrayOf(R.id.show_animation_btn, R.id.reset_animation_btn)

    // Массив id виджетов для показа разных анимаций
    private val animationWidgets = arrayOf(
            R.id.fade_animation_widget,
            R.id.pulse_animation_widget,
            R.id.new_size_animation_widget
    )

    @Before
    fun launchActivity() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testAnimations() {
        performClick(R.id.show_cross_fade_animation_btn, R.id.reset_cross_fade_animation_btn)

        animationWidgets.forEach {
            performClickOnWidget(it)
            onView(withId(it)).perform(nestedScrollTo())
        }

        // Последнюю анимацию тестируем отдельно, так как до виджета необходимо доскроллить
        onView(withId(R.id.slide_animation_widget)).perform(nestedScrollTo())
        performClickOnWidget(R.id.slide_animation_widget)
    }

    @Test
    fun testButtonClick() {
        performClick(R.id.bottom_btn)
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(isDisplayed()))
    }

    private fun performClickOnWidget(@IdRes widgetResId: Int) {
        widgetAnimationButtons.forEach {
            Espresso.onView(CoreMatchers.allOf(
                    ViewMatchers.withId(it),
                    ViewMatchers.withParent(ViewMatchers.withId(widgetResId)))
            ).perform(ViewActions.click())
        }
    }
}