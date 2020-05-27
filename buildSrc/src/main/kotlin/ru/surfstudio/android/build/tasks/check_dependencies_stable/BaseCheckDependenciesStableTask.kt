package ru.surfstudio.android.build.tasks.check_dependencies_stable

import org.gradle.api.DefaultTask
import ru.surfstudio.android.build.exceptions.library.LibraryNotFoundException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library

/**
 * Base class for check dependencies stable tasks
 */
open class BaseCheckDependenciesStableTask : DefaultTask() {

    /**
     * Check component library's standard dependencies for stability
     *
     * @return unstable libraries
     */
    protected fun checkComponent(component: Component): Set<UnstableLib> {
        val unstableLibraries = HashSet<UnstableLib>()

        component.libraries.forEach { library ->
            unstableLibraries += checkLibrary(library)
        }

        return unstableLibraries
    }

    private fun checkLibrary(library: Library): Set<UnstableLib> {
        val unstableLibs = HashSet<UnstableLib>()

        library.androidStandardDependencies.forEach { dependency ->
            if (!dependency.component.stable) {
                unstableLibs += UnstableLib(library.name).apply {
                    unstableDepenNames.add(dependency.name)
                }
            } else {
                val lib = dependency.component.libraries.find { it.name == dependency.name }
                        ?: throw LibraryNotFoundException(dependency.name)
                unstableLibs += checkLibrary(lib)
            }
        }

        return unstableLibs
    }

    protected data class UnstableLib(val libName: String) {

        val unstableDepenNames: ArrayList<String> = ArrayList()

        override fun toString() = "Library : \"$libName\" has unstable deps : $unstableDepenNames"

    }
}