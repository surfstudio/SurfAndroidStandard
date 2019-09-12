package ru.surfstudio.android.broadcast.extension.sample

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import ru.surfstudio.android.broadcast.extension.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkAndInputText
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

private const val PHONE_NUMBER = "1234"
private const val MESSAGE_TEXT = " text"

class BroadcastExtensionSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
    )

    @Test
    fun testInputFields() {
        checkAndInputText(R.id.number_et, R.string.number_et_hint, true, PHONE_NUMBER)
        checkAndInputText(R.id.message_et, R.string.message_et_text, false, MESSAGE_TEXT)
        performClick(R.id.send_btn)
    }
}