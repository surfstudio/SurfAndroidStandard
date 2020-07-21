package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import java.io.File
import java.io.InputStream

const val README_PATH = "README.md"
const val BUILD_STATUS_LINK_MD = "[build_status_link]: "
const val BUILD_ICON_MD = "[build_icon]: "

const val BUILD_STATUS_SUCCESS_ICON = "https://img.shields.io/badge/build-success-brightgreen"
const val BUILD_STATUS_FAILURE_ICON = "https://img.shields.io/badge/build-failure-red"
const val BUILD_STATUS_UNSTABLE_ICON = "https://img.shields.io/badge/build-unstable-yellow"

/**
 * Task for updating documentation
 */
open class UpdateBuildLinksForMainReadmeTask : DefaultTask() {

    private lateinit var buildUrl: String
    private var isBuildSuccessful: Boolean = false
    private var isBuildUnstable: Boolean = false

    @TaskAction
    fun create() {
        extractInputArguments()

        updateReadme()
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(GradleProperties.BUILD_URL)) {
            throw GradleException("please specify ${GradleProperties.BUILD_URL} param")
        }
        buildUrl = project.findProperty(GradleProperties.BUILD_URL) as String

        if (!project.hasProperty(GradleProperties.IS_BUILD_SUCCESSFUL)) {
            throw GradleException("please specify ${GradleProperties.IS_BUILD_SUCCESSFUL} param")
        }
        isBuildSuccessful = project.findProperty(GradleProperties.IS_BUILD_SUCCESSFUL) as Boolean

        if (!project.hasProperty(GradleProperties.IS_BUILD_UNSTABLE)) {
            throw GradleException("please specify ${GradleProperties.IS_BUILD_UNSTABLE} param")
        }
        isBuildUnstable = project.findProperty(GradleProperties.IS_BUILD_UNSTABLE) as Boolean
    }

    private fun updateReadme() {
        try {
            val docFile = File(README_PATH)
            val inputStream: InputStream = docFile.inputStream()
            var text = inputStream.bufferedReader().use { it.readText() }

            text.lines().forEach {
                if (it.contains(BUILD_STATUS_LINK_MD)) {
                    it.map { BUILD_STATUS_LINK_MD + buildUrl }
                }
                if (it.contains(BUILD_ICON_MD)) {
                    it.map {
                        when {
                            isBuildSuccessful -> {
                                BUILD_ICON_MD + BUILD_STATUS_SUCCESS_ICON
                            }
                            isBuildUnstable -> {
                                BUILD_ICON_MD + BUILD_STATUS_UNSTABLE_ICON
                            }
                            else -> {
                                BUILD_ICON_MD + BUILD_STATUS_FAILURE_ICON
                            }
                        }
                    }
                }
            }

            docFile.writeText(text)
        } catch (e: Exception) {
            throw GradleException("Error occurred while README updating")
        }
    }
}