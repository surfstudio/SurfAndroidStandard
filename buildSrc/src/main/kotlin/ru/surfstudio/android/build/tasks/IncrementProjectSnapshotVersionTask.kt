package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.ConfigInfoProvider

/**
 * Increment project snapshot version
 */
open class IncrementProjectSnapshotVersionTask : DefaultTask() {

    @TaskAction
    fun increment() {
        ConfigInfoProvider.incrementProjectSnapshotVersion()
    }
}