package ru.surfstudio.android.build.tasks.check_components

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Initializator.Companion.components
import ru.surfstudio.android.build.model.Component

const val ARG_FIRST_REV = "firstRevision"
const val ARG_SECOND_REV = "secondRevision"
private val CURRENT_DIR_PROPERTY = "user.dir"
val currentDirectory: String = System.getProperty(CURRENT_DIR_PROPERTY)


open class CheckStableComponentsChanged : DefaultTask() {
    val TASK_NAME = "CheckStableComponentsChanged: "

    private lateinit var firstRevision: String
    private lateinit var secondRevision: String

    @TaskAction
    fun check() {
        println("$TASK_NAME started")

        getInputArguments()

        val components = ComponentsConfigChecker(firstRevision, secondRevision).getChangedComponents()

        components.forEach { checkComponentIsStable(it) }

        println("$TASK_NAME ended")
    }

    private fun getInputArguments(){
        if (!project.hasProperty(ARG_FIRST_REV)) throw GradleException("please specify $ARG_FIRST_REV param")
        if (!project.hasProperty(ARG_SECOND_REV)) throw GradleException("please specify $ARG_SECOND_REV param")
        firstRevision = project.findProperty(ARG_FIRST_REV) as String
        secondRevision = project.findProperty(ARG_SECOND_REV) as String
    }

    private fun checkComponentIsStable(currentComponent: Component) {
        if (currentComponent.stable) {
            failStable(currentComponent)
        }
    }

    private fun failStable(currentComponent: Component) {
        throw GradleException(currentComponent.id + " is stable but was changed")
    }
}