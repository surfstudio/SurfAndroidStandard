package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.json.JsonDependency

/**
 * Represent information about dependency
 */
data class Dependency(
        val name: String = EMPTY_STRING,
        val type: String = EMPTY_STRING
) {

    companion object {

        fun create(jsonDependency: JsonDependency) = Dependency(
                name = jsonDependency.name,
                type = jsonDependency.type
        )
    }
}