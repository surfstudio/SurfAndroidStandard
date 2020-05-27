package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Can't find module in components.name
 */
class ModuleNotFoundInComponentsJsonException(moduleName: String) : GradleException(
        "Can't find module \"$moduleName\" in components.json"
)