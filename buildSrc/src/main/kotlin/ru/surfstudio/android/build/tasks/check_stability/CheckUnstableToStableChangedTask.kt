package ru.surfstudio.android.build.tasks.check_stability

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.tasks.changed_components.ProjectConfigurationProvider
import ru.surfstudio.android.build.utils.printRevisionsInfo

/**
 * Task check if any unstable component was changed to stable and fails if so
 */
open class CheckUnstableToStableChangedTask : DefaultTask() {

    private lateinit var revisionToCompare: String

    @TaskAction
    fun check() {
        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        printRevisionsInfo(currentRevision, revisionToCompare)

        val provider = ProjectConfigurationProvider(currentRevision, revisionToCompare)

        val currentProjectConfiguration = provider.provideCurrentRevisionConfiguration()
        val toCompareProjectConfiguration = provider.provideRevisionToCompareConfiguration()

        currentProjectConfiguration.components
                .filter { !it.isStable }
                .forEach { component ->
                    val componentToCompare = toCompareProjectConfiguration.components.find { it.id == component.id }
                            ?: return@forEach
                    if (componentToCompare.isStable) {
                        fail("${component.id} was unstable in current revision, but changed to stable in $revisionToCompare")
                    }
                }

        println("Unstable components did not become stable")
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            throw GradleException("please specify ${GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE} param")
        }
        revisionToCompare = project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
    }

    private fun fail(reason: String) {
        throw GradleException(reason)
    }
}