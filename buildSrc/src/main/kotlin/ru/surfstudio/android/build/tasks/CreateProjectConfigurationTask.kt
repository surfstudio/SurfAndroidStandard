package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties.CREATE_PROJECT_CONFIGURATION_PATH_TO_FILE
import ru.surfstudio.android.build.GradleProperties.CREATE_PROJECT_CONFIGURATION_REVISION
import ru.surfstudio.android.build.tasks.changed_components.ProjectConfigurationCreator

/**
 * Task for creating project configuration information json file
 *
 * [pathToFile] path to project
 * [revision] git revision of project
 */
open class CreateProjectConfigurationTask : DefaultTask() {
    private lateinit var pathToFile: String
    private lateinit var revision: String

    @TaskAction
    fun create() {
        extractInputArguments()
        ProjectConfigurationCreator(revision, pathToFile).createProjectConfigurationFile()
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(CREATE_PROJECT_CONFIGURATION_PATH_TO_FILE))
            throw GradleException("please specify $CREATE_PROJECT_CONFIGURATION_PATH_TO_FILE param")
        pathToFile = project.findProperty(CREATE_PROJECT_CONFIGURATION_PATH_TO_FILE) as String

        if (!project.hasProperty(CREATE_PROJECT_CONFIGURATION_REVISION))
            throw GradleException("please specify $CREATE_PROJECT_CONFIGURATION_REVISION param")
        revision = project.findProperty(CREATE_PROJECT_CONFIGURATION_REVISION) as String
    }
}