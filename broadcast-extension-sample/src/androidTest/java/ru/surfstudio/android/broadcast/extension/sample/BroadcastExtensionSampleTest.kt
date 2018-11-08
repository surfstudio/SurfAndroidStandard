package ru.surfstudio.android.broadcast.extension.sample

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.broadcast.extension.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.checkAndInputText

private const val PHONE_NUMBER = "123"
private const val MESSAGE_TEXT = " text"

@RunWith(AndroidJUnit4::class)
@SmallTest
class BroadcastExtensionSampleTest {

    @Before
    fun launchActivity() {
        ActivityScenario.launch(MainActivityView::class.java)
    }

    @Test
    fun testInputFields() {
        checkAndInputText(R.id.number_et, R.string.number_et_hint, true, PHONE_NUMBER)
        checkAndInputText(R.id.message_et, R.string.message_et_text, false, MESSAGE_TEXT)
    }
}