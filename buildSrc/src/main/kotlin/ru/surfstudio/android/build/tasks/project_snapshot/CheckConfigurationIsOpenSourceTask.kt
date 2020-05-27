package ru.surfstudio.android.build.tasks.project_snapshot

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.ConfigInfoProvider
import ru.surfstudio.android.build.exceptions.project_snapshot.ConfigurationIsProjectSnapshotException
import ru.surfstudio.android.build.exceptions.project_snapshot.ConfigurationNotProjectSnapshotException

/**
 * Check configuration is open source
 *
 * @throws ConfigurationNotProjectSnapshotException
 */
open class CheckConfigurationIsOpenSourceTask : DefaultTask() {

    @TaskAction
    fun check() {
        if (ConfigInfoProvider.globalConfigInfo.projectSnapshotName.isNotEmpty()) {
            throw ConfigurationIsProjectSnapshotException()
        }
    }
}