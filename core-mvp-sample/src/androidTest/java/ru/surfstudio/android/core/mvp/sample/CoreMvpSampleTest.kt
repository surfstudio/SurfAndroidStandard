package ru.surfstudio.android.core.mvp.sample

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.core.mvp.sample.ui.screen.another.AnotherActivityView
import ru.surfstudio.android.core.mvp.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.performClick

@RunWith(AndroidJUnit4::class)
@SmallTest
class CoreMvpSampleTest {

    @Before
    fun setUp() {
        Intents.init()
        launchActivity(MainActivityView::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testOpenAnotherScreen() {
        performClick(R.id.open_another_screen_btn)
        checkIfActivityIsVisible(AnotherActivityView::class.java)
    }
}