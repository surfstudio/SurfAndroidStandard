package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.exceptions.ComponentUnstableException
import ru.surfstudio.android.build.utils.getComponent

/**
 * Check component stable
 */
open class CheckComponentStableTask : DefaultTask() {

    /**
     * Check component stable
     */
    @TaskAction
    fun check() {
        val component = project.getComponent()

        if (!component.stable) throw ComponentUnstableException(component.name)
    }
}