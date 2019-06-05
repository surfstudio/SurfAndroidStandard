package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Component didn't find in components.json
 */
class ComponentNotFoundException(componentName: String) : GradleException(
        "Component \"$componentName\" not found in components.json"
)