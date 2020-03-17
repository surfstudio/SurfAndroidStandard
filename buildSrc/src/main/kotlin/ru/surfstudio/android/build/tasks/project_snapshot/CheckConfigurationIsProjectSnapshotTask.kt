package ru.surfstudio.android.build.tasks.project_snapshot

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.ConfigInfoProvider
import ru.surfstudio.android.build.exceptions.ConfigurationNotProjectSnapshotException

/**
 * Check configuration is project snapshot
 *
 * @throws ConfigurationNotProjectSnapshotException
 */
open class CheckConfigurationIsProjectSnapshotTask : DefaultTask() {

    @TaskAction
    fun check() {
        if (ConfigInfoProvider.globalConfigInfo.projectSnapshotName.isEmpty()) {
            throw ConfigurationNotProjectSnapshotException()
        }
    }
}