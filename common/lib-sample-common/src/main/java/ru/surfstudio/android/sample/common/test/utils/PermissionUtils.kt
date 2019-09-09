package ru.surfstudio.android.sample.common.test.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Утилиты для работы с разрешениями для инструментальных тестов
 */
object PermissionUtils {

    fun grantPermissions(vararg permissionNameList: String) {
        if (SdkUtils.isAtLeastMarshmallow()) {
            val command = "pm grant ${getApplicationContext<Context>().packageName} ${permissionNameList.joinToString()}"
            getInstrumentation().uiAutomation.executeShellCommand(command)
        }
    }
}