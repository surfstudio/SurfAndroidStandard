package ru.surfstudio.android.build.tasks

import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.exceptions.DependentComponentsUnstableException

/**
 * Check components libraries depend on parameter component libraries stable
 */
open class CheckDependentComponentStableTask : BaseCheckDependentComponentTask() {

    @TaskAction
    fun check() {
        val unstableLibraries = getDependentComponentLibraryNames(false)
        if (unstableLibraries.isNotEmpty()) throw DependentComponentsUnstableException(unstableLibraries)
    }
}