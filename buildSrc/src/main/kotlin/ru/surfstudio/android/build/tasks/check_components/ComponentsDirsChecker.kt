package ru.surfstudio.android.build.tasks.check_components

import ru.surfstudio.android.build.Initializator.Companion.components
import ru.surfstudio.android.build.model.Component

class ComponentsDirsChecker(val firstRevision: String,
                            val secondRevision: String
) : ComponentsChecker {
    override fun getChangedComponents(): List<Component> {
        val diffResults = GitExecutor().diff(firstRevision, secondRevision)

        return if (diffResults.isNullOrEmpty()) emptyList()
        else components.filter { currentComponent ->
            isComponentChanged(currentComponent, diffResults)
        }
    }

    private fun isComponentChanged(currentComponent: Component, diffResults: List<String>): Boolean {
        return currentComponent.libs.filter { library ->
            val libraryDir = "${currentComponent.dir}//${library.dir}"
            return diffResults.find { s -> s.contains(libraryDir, ignoreCase = true) } != null
        }.isNotEmpty()
    }
}