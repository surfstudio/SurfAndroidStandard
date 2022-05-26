package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.BaseGitRepository
import ru.surfstudio.android.build.ConfigInfoProvider
import ru.surfstudio.android.build.model.Component
import java.io.File

/**
 * Provides [components] (those only that have diff results between [currentRevision] and [revisionToCompare] )
 * mapped with those diff results
 */
class ComponentsDiffProvider(
    private val currentRevision: String,
    private val revisionToCompare: String,
    private val components: List<Component>
) {

    /**
     * provides mapping consisting of component with diff result
     *
     * @return list of pairs with components and corresponding diffs
     */
    fun provideComponentsWithDiff(ignoreNotLibFiles: Boolean = false): Map<Component, List<String>> {
        val diffResults = getDiffBetweenRevisions()
        return if (diffResults.isNullOrEmpty()) {
            emptyMap()
        } else {
            createComponentsWithDiff(diffResults, components, ignoreNotLibFiles)
        }
    }

    /**
     * return results for git diff command for [currentRevision] and [revisionToCompare]
     *
     * @return output from git diff command
     */
    private fun getDiffBetweenRevisions(): List<String>? {
        val repository = object : BaseGitRepository() {
            override val repositoryPath = File(ConfigInfoProvider.currentDirectory)
            override val repositoryName = "SurfAndroidStandard"
        }
        return repository.diff(revisionToCompare)
    }

    /**
     * maps component with corresponding diff results, only for components that have diff results
     *
     * @param components components for map
     * @param diffResults results for git diff command
     *
     * @return  map  with component and its diff results
     */
    private fun createComponentsWithDiff(
        diffResults: List<String>,
        components: List<Component>,
        ignoreNotLibFiles: Boolean = false
    ) = components
        .filter { component ->
            diffResults.any { isDiffHasComponent(it, component, ignoreNotLibFiles) }
        }
        .map { component ->
            val componentsDiffs =
                diffResults.filter { isDiffHasComponent(it, component, ignoreNotLibFiles) }
            component to componentsDiffs
        }
        .toMap()

    private fun isDiffHasComponent(
        diffResult: String,
        component: Component,
        ignoreNotLibFiles: Boolean = false
    ): Boolean {
        return diffResult.startsWith(component.directory)
                && if (diffResult.endsWith(RELEASE_NOTES_FILE_NAME) || diffResult.contains(
                SAMPLE_FILE_REGEX
            )
        ) !ignoreNotLibFiles else true
    }

    companion object {
        const val SAMPLE_FILE_REGEX = "/sample/"
        const val RELEASE_NOTES_FILE_NAME = "RELEASE_NOTES.md"
    }
}