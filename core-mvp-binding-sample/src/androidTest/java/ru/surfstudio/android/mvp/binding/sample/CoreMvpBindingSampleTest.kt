package ru.surfstudio.android.mvp.binding.sample

import org.junit.Test
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.mvp.binding.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible

class CoreMvpBindingSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testClickActions() {
        performClick(
                "1",
                R.id.pane_1,
                R.id.pane_2,
                R.id.pane_3,
                R.id.pane_4,
                R.id.pane_5,
                R.id.pane_6,
                R.id.pane_7,
                R.id.pane_8,
                R.id.pane_9
        )
        performClick(R.id.easy_win_btn)
        checkIfToastIsVisible(R.string.win_message)
        performClick(R.id.unbind_btn)
    }
}