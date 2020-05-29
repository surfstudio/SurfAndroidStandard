package ru.surfstudio.android.build.tasks.check_dependencies_stable

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.exceptions.UnstableLibraryDependenciesException
import ru.surfstudio.android.build.model.Component

/**
 * Task to check stable module dependencies stability
 */
open class CheckStableModuleDependenciesAreAlsoStableTask : BaseCheckDependenciesStableTask() {

    /**
     * Check library's standard dependencies for stability
     *
     * @throws GradleException if some dependencies unstable
     */
    @TaskAction
    fun check() {
        val unstableLibs = HashSet<UnstableLib>()

        Components.value
                .filter(Component::stable)
                .forEach { unstableLibs.addAll(checkComponent(it)) }

        if (unstableLibs.isNotEmpty()) throw UnstableLibraryDependenciesException(unstableLibs.toString())
    }
}