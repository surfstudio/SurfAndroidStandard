package ru.surfstudio.android.build.tasks.check_dependencies_stable

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.exceptions.UnstableLibraryDependenciesException
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Task to check component dependencies stability
 */
open class CheckComponentDependenciesAreStableTask : BaseCheckDependenciesStableTask() {

    /**
     * Check library's standard dependencies for stability
     *
     * @throws GradleException if some dependencies unstable
     */
    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()

        val unstableLibs = checkComponent(component)

        if (unstableLibs.isNotEmpty()) throw UnstableLibraryDependenciesException(unstableLibs.toString())
    }
}