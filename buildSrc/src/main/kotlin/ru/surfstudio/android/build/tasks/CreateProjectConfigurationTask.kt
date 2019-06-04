package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.tasks.changed_components.PATH_TO_FILE
import ru.surfstudio.android.build.tasks.changed_components.ProjectConfigurationCreator
import ru.surfstudio.android.build.tasks.changed_components.REVISION


open class CreateProjectConfigurationTask : DefaultTask() {
    private lateinit var pathToFile: String
    private lateinit var revision: String

    @TaskAction
    fun create() {
        extractInputArguments()
        ProjectConfigurationCreator(revision, pathToFile).createProjectConfigurationFile()
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(PATH_TO_FILE)) throw GradleException("please specify $PATH_TO_FILE param")
        pathToFile = project.findProperty(PATH_TO_FILE) as String
        if (!project.hasProperty(REVISION)) throw GradleException("please specify $REVISION param")
        revision = project.findProperty(REVISION) as String
    }
}