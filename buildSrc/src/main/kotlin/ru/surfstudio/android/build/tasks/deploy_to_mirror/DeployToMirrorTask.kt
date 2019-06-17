package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.LocalMirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.extractProperty
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Deploy to mirror
 */
open class DeployToMirrorTask : DefaultTask() {

    private lateinit var component: Component
    private lateinit var commitTo: String
    private lateinit var mirrorUrl: String
    private var depthLimit: Int = 0
    private var searchLimit: Int = 0

    @TaskAction
    fun deployToMirror() {
        extractParams()

        val mirrorRepository = LocalMirrorRepository(mirrorUrl)
        val standardRepository = StandardRepository()

        try {
            val branchName = standardRepository.getBranchByCommit(commitTo)
            val commitFrom = mirrorRepository.getLastCommit(branchName)

            val gitManager = GitDiffManager(standardRepository, mirrorRepository)
            gitManager.resolve(branchName, commitFrom, commitTo)

        } finally {
            mirrorRepository.delete()
        }
    }

    private fun extractParams() {
        component = project.getPropertyComponent()
        commitTo = project.extractProperty(GradleProperties.COMMIT)
        mirrorUrl = project.extractProperty(GradleProperties.MIRROR_URL)
        depthLimit = project.extractProperty(GradleProperties.DEPLOY_TO_MIRROR_DEPTH_LIMIT).toInt()
        searchLimit = project.extractProperty(GradleProperties.DEPLOY_TO_MIRROR_SEARCH_LIMIT).toInt()
    }
}