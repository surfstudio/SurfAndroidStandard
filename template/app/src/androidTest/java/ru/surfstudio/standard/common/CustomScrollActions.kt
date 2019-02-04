package ru.surfstudio.standard.common

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/**
 * ViewAction, позволяющий выполнять scrollTo() для NestedScrollView
 */
fun nestedScrollTo(): ViewAction = object : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
                isDescendantOfA(isAssignableFrom(NestedScrollView::class.java)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
    }

    override fun getDescription(): String {
        return "Find parent with type " + NestedScrollView::class.java +
                " of matched view and programmatically scroll to it."
    }

    override fun perform(uiController: UiController, view: View) {
        try {
            val nestedScrollView = findFirstParentLayoutOfClass(view, NestedScrollView::class.java) as NestedScrollView?
            if (nestedScrollView != null) {
                val coordinatorLayout = findFirstParentLayoutOfClass(view, CoordinatorLayout::class.java) as CoordinatorLayout?
                if (coordinatorLayout != null) {
                    val collapsingToolbarLayout = findCollapsingToolbarLayoutChildIn(coordinatorLayout)
                    if (collapsingToolbarLayout != null) {
                        val toolbarHeight = collapsingToolbarLayout.height
                        nestedScrollView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                        nestedScrollView.dispatchNestedPreScroll(0, toolbarHeight, null, null)
                    }
                }
                nestedScrollView.scrollTo(0, view.top)
            } else {
                throw Exception("Unable to find NestedScrollView parent.")
            }
        } catch (e: Exception) {
            throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(e)
                    .build()
        }

        uiController.loopMainThreadUntilIdle()
    }
}

private fun findCollapsingToolbarLayoutChildIn(viewGroup: ViewGroup): CollapsingToolbarLayout? {
    for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)
        if (child is CollapsingToolbarLayout) {
            return child
        } else if (child is ViewGroup) {
            return findCollapsingToolbarLayoutChildIn(child)
        }
    }
    return null
}

private fun findFirstParentLayoutOfClass(view: View, parentClass: Class<out View>): View? {
    var parent: ViewParent? = FrameLayout(view.context)
    var incrementView: ViewParent? = null
    var i = 0
    while (parent != null && parent.javaClass != parentClass) {
        if (i == 0) {
            parent = findParent(view)
        } else {
            parent = findParent(incrementView!!)
        }
        incrementView = parent
        i++
    }
    return parent as View?
}

private fun findParent(view: View): ViewParent = view.parent

private fun findParent(view: ViewParent): ViewParent = view.parent