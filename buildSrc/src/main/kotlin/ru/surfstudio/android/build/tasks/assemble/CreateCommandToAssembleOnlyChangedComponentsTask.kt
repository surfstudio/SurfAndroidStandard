package ru.surfstudio.android.build.tasks.assemble

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.ComponentsConfigurationChecker
import ru.surfstudio.android.build.tasks.changed_components.ComponentsFilesChecker
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentChangeReason
import java.io.File

/**
 * Find changed components and create command to assemble only changed and dependent from changed
 */
open class CreateCommandToAssembleOnlyChangedComponentsTask : DefaultTask() {

    private lateinit var revisionToCompare: String

    private val currentComponents = Components.value
    private val changedComponentsMap = mutableMapOf<Component?, ComponentChangeReason>()

    @TaskAction
    fun createAssembleCommand() {
        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        changedComponentsMap += ComponentsConfigurationChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
                .filter { componentCheckResult -> componentCheckResult.isComponentChanged }
                .map { componentCheckResult ->
                    currentComponents.find { it.name == componentCheckResult.componentName } to componentCheckResult.reasonOfComponentChange
                }

        changedComponentsMap += ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
                .filter { componentCheckResult -> componentCheckResult.isComponentChanged }
                .map { componentCheckResult ->
                    currentComponents.find { it.name == componentCheckResult.componentName } to componentCheckResult.reasonOfComponentChange
                }

        addDependentComponents()

        writeAssembleCommand(changedComponentsMap.toMap())
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            throw GradleException("please specify ${GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE} param")
        }
        revisionToCompare = project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
    }

    /**
     * Find and add components which depend on changedComponents
     */
    private fun addDependentComponents() {
        val changedComponentsNames = changedComponentsMap.keys.map {
            it?.name
        }

        currentComponents.forEach { component ->
            component.libraries.forEach { library ->
                library.androidStandardDependencies.forEach { androidStandardDependency ->
                    if (changedComponentsNames.contains(androidStandardDependency.name)) {
                        changedComponentsMap += component to ComponentChangeReason.NO_REASON
                    }
                }
            }
        }
    }

    private fun writeAssembleCommand(changedComponents: Map<Component?, ComponentChangeReason>) {
        val assembleCommand = createOutputForChangedComponents(changedComponents)

        val file = File(ASSEMBLE_COMMAND_FILE_NAME)
        with(file) {
            if (exists()) {
                delete()
            }
            createNewFile()
            appendText(assembleCommand)
        }
    }

    private fun createOutputForChangedComponents(results: Map<Component?, ComponentChangeReason>): String {
        return results
                .filter { it.value != ComponentChangeReason.COMPONENT_REMOVED }
                .keys
                .joinToString(separator = " ") { component ->

                    val librariesAssembleCommand = component?.libraries?.joinToString(separator = " ") { library ->
                        ":${library.name}:assemble"
                    }

                    val samplesAssembleCommand = component?.samples?.joinToString(separator = " ") { sample ->
                        ":${sample.name}:assemble"
                    }

                    return@joinToString "$librariesAssembleCommand $samplesAssembleCommand"
                }
    }

    companion object {
        const val ASSEMBLE_COMMAND_FILE_NAME = "buildSrc/build/tmp/assembleCommand.txt"
    }
}