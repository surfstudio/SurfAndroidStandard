package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import java.lang.StringBuilder

/**
 * Task to check standard library's dependencies
 */
open class CheckStandardDependenciesStability : DefaultTask() {

    /**
     * Check library's standard dependencies for stability
     *
     * @throws GradleException if some dependencies unstable
     */
    @TaskAction
    fun check() {
        var hasUnstableDeps = false
        val messageBuilder = StringBuilder()
        Components.value.forEach { component ->
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