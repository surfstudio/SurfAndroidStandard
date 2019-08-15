package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.exceptions.PropertyNotDefineException
import ru.surfstudio.android.build.exceptions.PropertyVersionsNotEqualsComponentVersionException
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check version from property equals version from component
 */
open class CheckVersionEqualsComponentVersionTask : DefaultTask() {

    /**
     * Check version from property equals version from component
     *
     * @throws PropertyVersionsNotEqualsComponentVersionException is version don't equals
     */
    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()

        val propertyVersion = project.property(GradleProperties.COMPONENT_VERSION) as? String
                ?: throw PropertyNotDefineException(GradleProperties.COMPONENT_VERSION)

        if (component.baseVersion != propertyVersion) {
            throw PropertyVersionsNotEqualsComponentVersionException(
                    component.name,
                    component.projectVersion,
                    propertyVersion
            )
        }
    }
}