package ru.surfstudio.android.firebase.sample

import org.junit.Test
import ru.surfstudio.android.firebase.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkAndInputText
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class FirebaseSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    private val inputText = " test"

    @Test
    fun testFirebaseSample() {
        checkAndInputText(R.id.event_text_et, R.string.event_text_et_value, false, inputText)
        performClick(R.id.send_event_btn)
    }
}