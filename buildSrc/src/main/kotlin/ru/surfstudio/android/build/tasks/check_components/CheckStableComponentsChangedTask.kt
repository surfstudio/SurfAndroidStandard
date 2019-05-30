package ru.surfstudio.android.build.tasks.check_components

import com.beust.klaxon.Klaxon
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.model.Component
import java.io.File

const val REVISION_TO_COMPARE = "revisionToCompare"
const val CURRENT_DIR_PROPERTY = "user.dir"

val currentDirectory: String = System.getProperty(CURRENT_DIR_PROPERTY)


/**
 * Task checking if stable components in current revision compared to [revisionToCompare] are changed
 */
open class CheckStableComponentsChanged : DefaultTask() {
    private val TASK_NAME = "CheckStableComponentsChanged: "

    private lateinit var revisionToCompare: String

    @TaskAction
    fun check() {
        println("$TASK_NAME started")

        getInputArguments()
        val currentRevision = GitCommandRunner(currentDirectory).getCurrentRevisionShort()

        val filesCheckResult = ComponentsFilesChecker(currentRevision, revisionToCompare).checkComponentsFilesChanged()
        if (!filesCheckResult.isOk){
            fail(filesCheckResult.reasonFail)
        }
        val configCheckResult = ComponentsConfigChecker(currentRevision, revisionToCompare).checkComponentsConfigChanged()
        if (!configCheckResult.isOk){
            fail(configCheckResult.reasonFail)
        }

        println("$TASK_NAME ended")
    }

    private fun getInputArguments(){
        if (!project.hasProperty(REVISION_TO_COMPARE)) throw GradleException("please specify $REVISION_TO_COMPARE param")
        revisionToCompare = project.findProperty(REVISION_TO_COMPARE) as String
    }

    private fun fail(reason: String) {
        throw GradleException(reason)
    }

    private fun parseComponentJson(path: String): List<Component> {
        return Klaxon().parseArray(File(path))
                ?: throw RuntimeException("Can't parse components.json")
    }
}