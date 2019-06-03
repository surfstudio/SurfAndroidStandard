package ru.surfstudio.android.build.model.json

import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.model.dependency.AndroidStandardDependency
import ru.surfstudio.android.build.model.dependency.ThirdPartyDependency

/**
 * Represent information about dependencies json object
 */
data class DependencyJson(
        val name: String = EMPTY_STRING,
        val type: String = EMPTY_STRING
) {

    fun transformToAndroidStandardDependency() = AndroidStandardDependency(
            name, type
    )

    fun transformToThirdPartyDependency() = ThirdPartyDependency(
            name, type
    )
}