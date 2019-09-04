package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.exceptions.ComponentUnstableException
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check component stable
 */
open class CheckComponentStableTask : DefaultTask() {

    /**
     * Check component stable
     */
    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()

        if (!component.stable) throw ComponentUnstableException(component.name)
    }
}