package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties.CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT
import ru.surfstudio.android.build.GradleProperties.CREATE_PROJECT_CONFIGURATION_REVISION
import ru.surfstudio.android.build.tasks.changed_components.ProjectConfigurationCreator

/**
 * Task for creating project configuration information json file
 */
open class CreateProjectConfigurationTask : DefaultTask() {

    private lateinit var pathToProject: String
    private lateinit var revision: String

    @TaskAction
    fun create() {
        extractInputArguments()
        ProjectConfigurationCreator(revision, pathToProject).createProjectConfigurationFile()
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT)) {
            throw GradleException("please specify $CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT param")
        }
        pathToProject = project.findProperty(CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT) as String

        if (!project.hasProperty(CREATE_PROJECT_CONFIGURATION_REVISION)) {
            throw GradleException("please specify $CREATE_PROJECT_CONFIGURATION_REVISION param")
        }
        revision = project.findProperty(CREATE_PROJECT_CONFIGURATION_REVISION) as String
    }
}