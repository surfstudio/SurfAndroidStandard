package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.utils.COMPONENTS_JSON_FILE_PATH
import ru.surfstudio.android.build.utils.JsonHelper
import ru.surfstudio.android.build.utils.getPropertyComponent
import java.io.File

/**
 * Set alpha to zero
 */
open class SetComponentAlphaCounterToZeroTask : DefaultTask() {

    @TaskAction
    fun setZero() {
        project.getPropertyComponent().unstableVersion = 0

        JsonHelper.write(Components.value.map { ComponentJson(it) }, File(COMPONENTS_JSON_FILE_PATH))
    }
}