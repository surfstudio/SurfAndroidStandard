package ru.surfstudio.android.mvpwidget.sample

import org.junit.Test
import ru.surfstudio.android.mvpwidget.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.ElapsedTimeIdlingResource
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.*

class MvpWidgetSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testOpenFragment() {
        ViewUtils.performClick(R.id.fragment_btn)
        VisibilityUtils.checkIfViewIsVisible(R.id.constraint_widget_view)
        VisibilityUtils.checkIfViewIsVisible(R.id.relative_widget_view)
        VisibilityUtils.checkIfViewIsVisible(R.id.linear_widget_view)
        VisibilityUtils.checkIfViewIsVisible(R.id.frame_widget_view)
    }

    @Test
    fun testClickOnWidget() {
        ViewUtils.performClick(R.id.fragment_btn)
        VisibilityUtils.checkIfViewIsVisible(R.id.constraint_widget_view)
        ViewUtils.performClick(R.id.constraint_widget_view)

        // необходима задержка, так как в presenter'e у widget'а используется подписка Observable.timer с задержкой 500мс
        val expectedDelay = 550L
        val expectedText = "Hello Constraint widget view its render from presenter"

        val idlingResource = ElapsedTimeIdlingResource(expectedDelay)
        IdlingUtils.registerIdlingResource(idlingResource)
        TextUtils.checkViewText(R.id.widget_tv, R.id.constraint_widget_view, expectedText)

        IdlingUtils.unregisterIdlingResource(idlingResource)
    }

    @Test
    fun testOpenRecyclerView() {
        ViewUtils.performClick(R.id.list_btn)
        VisibilityUtils.checkIfViewIsVisible(R.id.recycler)
    }

    @Test
    fun testClickInRecyclerView() {
        ViewUtils.performClick(R.id.list_btn)
        VisibilityUtils.checkIfViewIsVisible(R.id.recycler)
        RecyclerViewUtils.performItemClick(R.id.recycler, 0)
    }
}