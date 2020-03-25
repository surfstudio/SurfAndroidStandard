package ru.surfstudio.android.build.exceptions.component

import org.gradle.api.GradleException

/**
 * Can't find component for android standard dependency exeption
 */
class ComponentNotFoundForStandardDependencyException(dependencyName: String) : GradleException(
        "There isn't component for android standard dependency $dependencyName. " +
                "Please check components.json."
)