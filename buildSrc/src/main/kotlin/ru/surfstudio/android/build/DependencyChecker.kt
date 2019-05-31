package ru.surfstudio.android.build

import org.gradle.api.GradleException
import java.lang.StringBuilder

/**
 * Helper class to check build restrictions
 */
object DependencyChecker {

    /**
     * Check library's standard dependencies for stability
     *
     * @throws GradleException if some dependencies unstable
     */
    @JvmStatic
    fun checkDependencyStability() {
        var hasUnstableDeps = false
        var messageBuilder = StringBuilder()
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