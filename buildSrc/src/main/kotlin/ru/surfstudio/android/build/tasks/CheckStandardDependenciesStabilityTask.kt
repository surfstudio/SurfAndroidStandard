package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.readProperty

/**
 * Task to check standard library's dependencies
 */
open class CheckStandardDependenciesStabilityTask : DefaultTask() {

    /**
     * Check library's standard dependencies for stability
     *
     * @throws GradleException if some dependencies unstable
     */
    @TaskAction
    fun check() {
        if (!project.hasProperty(GradleProperties.COMPONENTS_TO_CHECK_STANDARD_DEPENDENCIES_STABILITY)) {
            return
        }

        val components = project.readProperty(
                GradleProperties.COMPONENTS_TO_CHECK_STANDARD_DEPENDENCIES_STABILITY,
                EMPTY_STRING
        ).split(",")

        if (components.isEmpty()) return

        var hasUnstableDeps = false
        val messageBuilder = StringBuilder()
        Components.value
                .filter { components.contains(it.name) }
                .forEach { component ->
                    component.libraries.forEach { library ->
                        library.androidStandardDependencies.forEach { dependency ->
                            if (!dependency.component.stable) {
                                hasUnstableDeps = true
                                messageBuilder.append("Library(\"${library.name}\") from component(\"${component.name}\") has unstable android standard dependency(\"${dependency.name}\")\n")
                            }
                        }
                    }
                }

        if (hasUnstableDeps) throw GradleException(messageBuilder.toString())
    }
}