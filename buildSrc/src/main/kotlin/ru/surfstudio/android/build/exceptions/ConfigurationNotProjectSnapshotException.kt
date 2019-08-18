package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Throw if project configuration is not project snapshot
 */
class ConfigurationNotProjectSnapshotException : GradleException(
        "Configuration isn't project snapshot. " +
                "Please define project_snapshot_name in projectConfiguration.json"
)