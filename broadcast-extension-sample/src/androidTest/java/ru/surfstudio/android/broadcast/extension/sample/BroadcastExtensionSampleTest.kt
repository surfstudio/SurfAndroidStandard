package ru.surfstudio.android.broadcast.extension.sample

import org.junit.Test
import ru.surfstudio.android.broadcast.extension.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkAndInputText

private const val PHONE_NUMBER = "123456789"
private const val MESSAGE_TEXT = " text"

class BroadcastExtensionSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testInputFields() {
        checkAndInputText(R.id.number_et, R.string.number_et_hint, true, PHONE_NUMBER)
        checkAndInputText(R.id.message_et, R.string.message_et_text, false, MESSAGE_TEXT)
    }
}