package ru.surfstudio.android.build.tasks.check_stable_artifacts_exist_in_repositories

import org.gradle.api.DefaultTask
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library

/**
 * Base task class for CheckStableArtifactsExistInRepository tasks
 */
open class BaseCheckStableArtifactsExistInRepositoryTask : DefaultTask() {

    fun findStableLibraries(): List<Library> = Components.value
            .filter(Component::stable)
            .flatMap(Component::libraries)
}