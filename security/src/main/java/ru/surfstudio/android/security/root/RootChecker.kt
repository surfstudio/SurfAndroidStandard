package ru.surfstudio.android.security.root

import ru.surfstudio.android.security.jni.Natives
import ru.surfstudio.android.security.root.error.RootDetectedException

/**
 * Объект, проверяющий наличие рут-прав на устройстве
 */
object RootChecker {
    val isRoot: Boolean = isRootInternal()

    private fun isRootInternal(): Boolean = try {
        Natives.init()
        false
    } catch (ignored: RootDetectedException) {
        true
    }
}