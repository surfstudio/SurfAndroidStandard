package ru.surfstudio.android.firebase.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.firebase.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.checkAndInputText
import ru.surfstudio.android.sample.common.test.utils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.performClick

@RunWith(AndroidJUnit4::class)
@SmallTest
class FirebaseSampleTest {

    private val inputText = " test"

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

    @Test
    fun testFirebaseSample() {
        checkAndInputText(R.id.event_text_et, R.string.event_text_et_value, false, inputText)
        performClick(R.id.send_event_btn)
    }
}