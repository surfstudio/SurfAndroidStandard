package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.model.Component

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
    fun provideComponentsWithDiff(): Map<Component, List<String>> {
        val diffResults = getDiffBetweenRevisions()
        return if (diffResults.isNullOrEmpty()) emptyMap()
        else createComponentsWithDiff(diffResults, components)
    }

    /**
     * return results for git diff command for [currentRevision] and [revisionToCompare]
     *
     * @return output from git diff command
     */
    private fun getDiffBetweenRevisions(): List<String>? {
        return GitCommandRunner().diff(currentRevision, revisionToCompare)
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
            components: List<Component>
    ) = components
            .filter { component ->
                diffResults.any { isDiffHasComponent(it, component) }
            }
            .map { component ->
                val componentsDiffs = diffResults.filter { isDiffHasComponent(it, component) }
                component to componentsDiffs
            }
            .toMap()

    private fun isDiffHasComponent(it: String, component: Component) =
            it.startsWith(component.directory)
}