package ru.surfstudio.android.build.tasks.check_stability

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.exceptions.component.DependentComponentsStableException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Only unstable component can depend on the new modified stable component
 */
open class CheckDependencyForComponentUnstableTask : DefaultTask() {

    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()
        val stableLibraries = mutableSetOf<Library>()

        component.libraries.forEach {
            checkDependentLibrariesStable(stableLibraries, it.name, component)
        }

        if (stableLibraries.isNotEmpty()) {
            throw DependentComponentsStableException(stableLibraries.map(Library::name))
        }
    }

    private fun checkDependentLibrariesStable(
        stableLibraries: MutableSet<Library>,
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