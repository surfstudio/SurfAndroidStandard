package ru.surfstudio.android.sample.common.test

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ToastMatcher : TypeSafeMatcher<Root>() {

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root): Boolean {
        // windowToken == appToken means this window isn't contained by any other windows.
        // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
        return root.windowLayoutParams.get().type == WindowManager.LayoutParams.TYPE_TOAST &&
                root.decorView.windowToken === root.decorView.applicationWindowToken
    }
}