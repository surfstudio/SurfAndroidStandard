package ru.surfstudio.android.build.exceptions.project_snapshot

import org.gradle.api.GradleException

/**
 * Throw if project configuration is project snapshot
 */
class ConfigurationIsProjectSnapshotException : GradleException(
        "Configuration is project snapshot. But this task can't be use for this pipeline."
)