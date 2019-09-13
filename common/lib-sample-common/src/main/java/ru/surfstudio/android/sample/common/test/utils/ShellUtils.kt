package ru.surfstudio.android.sample.common.test.utils

import androidx.test.platform.app.InstrumentationRegistry
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Утилиты для выполнения консольных команд на устойстве для запуска тестов
 */
object ShellUtils {

    fun executeCommands(vararg commandList: String) {
        if (SdkUtils.isAtLeastMarshmallow()) {
            commandList.forEach {
                InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(it)
            }
        }
    }
}