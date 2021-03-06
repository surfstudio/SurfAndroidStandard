package ru.surfstudio.android.recycler.extension.sample

import androidx.annotation.CallSuper
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Test
import ru.surfstudio.android.recycler.extension.sample.screen.SlidingItemsActivity
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.AnimationUtils

internal class RecyclerSlidingTest : BaseSampleTest<SlidingItemsActivity>(SlidingItemsActivity::class.java) {

    override fun setUp() {
        super.setUp()
        AnimationUtils.grantScaleAnimationPermission()
        AnimationUtils.disableAnimations()
    }

    @After
    @CallSuper
    fun tearDown() {
        AnimationUtils.enableAnimations()
    }

    @Test
    fun testSliding() {
        onView(
                allOf(
                        withId(R.id.sliding_item_title_tv),
                        withText("Item #1")
                )
        ).perform(ViewActions.swipeLeft())

        val itemSideButton = onView(
                allOf(
                        withId(R.id.sliding_right_buttons_container),
                        isDisplayed()
                )
        ).check(ViewAssertions.matches(isDisplayed()))

        onView(
                allOf(
                        withId(R.id.sliding_item_title_tv),
                        withText("Item #2")
                )
        ).perform(ViewActions.swipeUp())

        Thread.sleep(1000)

        itemSideButton.check(doesNotExist())
    }
}