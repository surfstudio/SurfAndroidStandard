package ru.surfstudio.android.sample.common.test.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException


/**
 * Общие утилиты для тестирования View
 */
object ViewUtils {

    /**
     * Функция, выполняющая нажатие на каждую вью, id которых переданы в параметрах
     */
    fun performClick(@IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it)).perform(click())
        }
    }

    /**
     * Функция, выполняющая нажатие на каждую вью, id которых переданы в параметрах,
     * и вызывающая другую функцию после клика на каждую вью
     */
    fun performClick(action: () -> Unit, @IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it)).perform(click())
            action()
        }
    }

    /**
     * Функция, выполняющая нажатие на каждую вью, id которых переданы в параметрах,
     * и проверяющая, что значение каждой вью после клика равно указанному тексту
     */
    fun performClick(textForCheck: String, @IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it))
                    .perform(click())
                    .check(matches(withText(textForCheck)))
        }
    }

    /** Perform action of waiting for a specific view id.  */
    fun waitId(viewId: Int, millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with id <$viewId> during $millis millis."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher = withId(viewId)

                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(TimeoutException())
                        .build()
            }
        }
    }
}