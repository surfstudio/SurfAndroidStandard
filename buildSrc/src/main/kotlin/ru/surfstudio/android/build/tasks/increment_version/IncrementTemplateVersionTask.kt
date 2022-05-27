package ru.surfstudio.android.build.tasks.increment_version

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.ConfigInfoProvider
import java.io.File

private const val TEMPLATE_GRADLE_PROPERTIES = "template/gradle.properties"
private const val TEMPLATE_ANDROID_STANDARD_VERSION = "androidStandardVersion"

/**
 * Increment template AS version
 */
open class IncrementTemplateVersionTask : DefaultTask() {

    @TaskAction
    fun increment() {
        var newString = ""
        File(TEMPLATE_GRADLE_PROPERTIES).forEachLine {
            newString += if (it.startsWith(TEMPLATE_ANDROID_STANDARD_VERSION)) {
                "$TEMPLATE_ANDROID_STANDARD_VERSION=${ConfigInfoProvider.globalConfigInfo.version}\n"
            } else {
                "$it\n"
            }
        }
        File(TEMPLATE_GRADLE_PROPERTIES).writeText(newString)
    }
}