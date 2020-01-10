package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.ConfigInfoProvider

/**
 * Increment global unstable version
 */
open class IncrementGlobalUnstableVersionTask : DefaultTask() {

    @TaskAction
    fun increment() {
        ConfigInfoProvider.incrementUnstableVersion()
    }
}