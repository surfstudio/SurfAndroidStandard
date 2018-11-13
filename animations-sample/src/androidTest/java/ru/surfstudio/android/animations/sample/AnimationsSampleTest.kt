package ru.surfstudio.android.animations.sample

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.checkIfSnackbarIsVisible
import ru.surfstudio.android.sample.common.test.utils.launchActivity
import ru.surfstudio.android.sample.common.test.nestedScrollTo
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
    fun setUp() {
        launchActivity(MainActivity::class.java)
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
        checkIfSnackbarIsVisible(R.string.snackbar_message)
    }

    private fun performClickOnWidget(@IdRes widgetResId: Int) {
        widgetAnimationButtons.forEach {
            onView(allOf(withId(it), withParent(withId(widgetResId))))
                    .perform(click())
        }
    }
}