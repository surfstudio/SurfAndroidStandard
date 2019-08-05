package ru.surfstudio.android.custom_view_sample

import android.view.View
import org.hamcrest.Matcher
import org.junit.Test
import ru.surfstudio.android.custom.view.bottomsheet.BottomSheetView

class CustomViewSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    override fun setUp() {
        super.setUp()
        AnimationUtils.grantScaleAnimationPermission()
        AnimationUtils.disableAnimations()
    }

    override fun tearDown() {
        super.tearDown()
        AnimationUtils.enableAnimations()
    }

    @Test
    fun testCustomViewSample() {
        checkIfActivityIsVisible(MainActivity::class.java)

        performClick(R.id.open_tv_screen_btn)
        checkIfActivityIsVisible(TitleSubtitleViewDemoActivity::class.java)
        Espresso.pressBack()

        performClick(R.id.change_state_btn)

        Espresso.onView(ViewMatchers.withId(R.id.example_bsv)).perform(object : ViewAction {

            override fun getDescription() = "Swipe up of bottomsheet view"

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(BottomSheetView::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val bsView = view as BottomSheetView
                bsView.expand()
            }
        })
        VisibilityUtils.checkIfViewIsVisible(R.id.top_title_tsv)
    }
}