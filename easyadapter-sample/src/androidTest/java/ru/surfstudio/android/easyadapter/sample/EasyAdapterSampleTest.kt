package ru.surfstudio.android.easyadapter.sample

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.easyadapter.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.easyadapter.sample.ui.screen.multitype.MultitypeListActivityView
import ru.surfstudio.android.sample.common.test.utils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.performClick

@RunWith(AndroidJUnit4::class)
@SmallTest
class EasyAdapterSampleTest {

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
    fun testEasyAdapterSample() {
        performClick(R.id.show_multitype_list_btn)
        checkIfActivityIsVisible(MultitypeListActivityView::class.java)
    }
}