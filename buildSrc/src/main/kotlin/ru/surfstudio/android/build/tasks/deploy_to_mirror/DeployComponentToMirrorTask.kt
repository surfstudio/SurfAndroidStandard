package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.extractProperty
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Task for deploying [component] to mirror
 */
open class DeployComponentToMirrorTask : DefaultTask() {

    private lateinit var component: Component
    private lateinit var commitHash: String
    private lateinit var mirrorDir: String
    private lateinit var mirrorUrl: String
    private var standardDepthLimit: Int = 0
    private var mirrorDepthLimit: Int = 0

    @TaskAction
    fun deployToMirror() {
        extractParams()

        val standardRepository = StandardRepository()
        val mirrorRepository = MirrorRepository(mirrorDir, mirrorUrl)
        val mirrorManager = MirrorManager(
                component.directory,
                standardRepository,
                mirrorRepository,
                standardDepthLimit,
                mirrorDepthLimit
        )

        try {
            mirrorManager.mirror(commitHash)
        } finally {
            mirrorRepository.delete()
            standardRepository.delete()
        }
    }

    private fun extractParams() {
        component = project.getPropertyComponent()
        commitHash = project.extractProperty(GradleProperties.COMMIT)
        mirrorDir = project.extractProperty(GradleProperties.MIRROR_DIR)
        mirrorUrl = project.extractProperty(GradleProperties.MIRROR_URL)
        standardDepthLimit = project.extractProperty(GradleProperties.DEPLOY_TO_MIRROR_DEPTH_LIMIT).toInt()
        mirrorDepthLimit = project.extractProperty(GradleProperties.DEPLOY_TO_MIRROR_SEARCH_LIMIT).toInt()
    }
}