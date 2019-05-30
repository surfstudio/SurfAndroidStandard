package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.json.DependencyJson

/**
 * Represent information about dependency
 */
data class Dependency(
        val name: String = EMPTY_STRING,
        val type: String = EMPTY_STRING
) {

    companion object {

        fun create(dependencyJson: DependencyJson) = Dependency(
                name = dependencyJson.name,
                type = dependencyJson.type
        )
    }
}