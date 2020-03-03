package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties.OVERRIDE_EXISTED
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.utils.getPropertyComponent
import ru.surfstudio.android.build.utils.getPropertyComponentName
import ru.surfstudio.android.build.utils.readProperty

/**
 * Distribute artifacts to Bintray
 */
open class DistributeArtifactsToBintrayTask : DefaultTask() {

    @TaskAction
    fun distribute() {
        val componentName = project.getPropertyComponentName()
        val overrideExisted = project.readProperty(OVERRIDE_EXISTED, "false").toBoolean()
        if (componentName != "all") {
            deployComponent(project.getPropertyComponent(componentName), overrideExisted)
        } else {
            Components.value.forEach {
                if (it.name != "mvp" && it.name != "security" && it.name != "template" && it.name != "docs" &&
                        it.name != "android-standard-version-plugin" && it.name != "android-studio-settings"
                ) {
                    deployComponent(it, overrideExisted)
                } else {
                    println("skip ${it.name} deploy")
                }
            }
        }
    }

    private fun deployComponent(component: Component, overrideExisted: Boolean) {
        println("deployComponent ${component.name}")
        Artifactory.distributeArtifactToBintray(overrideExisted, component.libraries.map(Library::name))
    }
}