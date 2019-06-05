package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.exceptions.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.PropertyNotDefineException
import ru.surfstudio.android.build.model.module.Library

/**
 * Base class for CheckDependentComponentTask tasks
 */
open class BaseCheckDependentComponentTask : DefaultTask() {

    /**
     * Check dependent component stability
     * @param stability - component stability
     * @return - library list that has invert stability
     */
    fun getDependentComponentLibraryNames(stability: Boolean): List<String> {
        val componentName = project.property(GradleProperties.COMPONENT) as? String
                ?: throw PropertyNotDefineException(GradleProperties.COMPONENT)
        val component = Components.value.find { it.name == componentName }
                ?: throw ComponentNotFoundException(componentName)
        val libraryNames = component.libraries.map(Library::name)
        val stableLibraries = ArrayList<String>()

        Components.value.forEach { comp ->
            comp.libraries.forEach { lib ->
                lib.androidStandardDependencies.forEach { dep ->
                    if (libraryNames.contains(dep.name) && comp.stable == stability) stableLibraries.add(lib.name)
                }
            }
        }

        return stableLibraries
    }
}