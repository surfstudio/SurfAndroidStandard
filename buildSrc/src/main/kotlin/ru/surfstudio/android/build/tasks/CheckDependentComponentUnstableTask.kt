package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.exceptions.DependentComponentsStableException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check components libraries depend on parameter component libraries unstable
 */
open class CheckDependencyForComponentUnstableTask : DefaultTask() {

    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()
        val stableLibraries = ArrayList<Library>()

        component.libraries.forEach {
            checkDependentLibrariesStable(stableLibraries, it.name, component)
        }

        if (stableLibraries.isNotEmpty()) {
            throw DependentComponentsStableException(stableLibraries.map(Library::name))
        }
    }

    private fun checkDependentLibrariesStable(
            stableLibraries: ArrayList<Library>,
            libName: String,
            checkedComponent: Component
    ) {
        Components.value.forEach { comp ->
            comp.libraries.forEach { lib ->
                lib.androidStandardDependencies.forEach { dep ->
                    if (libName == dep.name) {
                        if (comp.stable && comp.name != checkedComponent.name) {
                            stableLibraries.add(lib)
                        }
                        checkDependentLibrariesStable(stableLibraries, lib.name, checkedComponent)
                    }
                }
            }
        }
    }
}