package ru.surfstudio.android.mvp.binding.sample

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.mvp.binding.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible
import ru.surfstudio.android.sample.common.test.utils.performClick

@RunWith(AndroidJUnit4::class)
@SmallTest
class CoreMvpBindingSampleTest {

    @Before
    fun launchActivity() {
        ActivityScenario.launch(MainActivityView::class.java)
    }

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
    }
}