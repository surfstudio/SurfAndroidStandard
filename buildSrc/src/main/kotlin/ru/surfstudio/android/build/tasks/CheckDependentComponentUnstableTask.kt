package ru.surfstudio.android.build.tasks

import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.exceptions.DependentComponentsStableException

/**
 * Check components libraries depend on parameter component libraries unstable
 */
open class CheckDependentComponentUnstableTask : BaseCheckDependentComponentTask() {

    @TaskAction
    fun check() {
        val stableLibraries = getDependentComponentLibraryNames(true)
        if (stableLibraries.isNotEmpty()) throw DependentComponentsStableException(stableLibraries)
    }
}