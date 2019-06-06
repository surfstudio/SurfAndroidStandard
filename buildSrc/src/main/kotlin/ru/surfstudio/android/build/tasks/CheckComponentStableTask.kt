package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.exceptions.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.ComponentUnstableException
import ru.surfstudio.android.build.exceptions.PropertyNotDefineException

/**
 * Check component stable
 */
open class CheckComponentStableTask : DefaultTask() {

    /**
     * Check component stable
     */
    @TaskAction
    fun check() {
        val componentName = project.property(GradleProperties.COMPONENT) as? String
                ?: throw PropertyNotDefineException(GradleProperties.COMPONENT)
        val component = Components.value.find { it.name == componentName }
                ?: throw ComponentNotFoundException(componentName)
        if (!component.stable) throw ComponentUnstableException(componentName)
    }
}