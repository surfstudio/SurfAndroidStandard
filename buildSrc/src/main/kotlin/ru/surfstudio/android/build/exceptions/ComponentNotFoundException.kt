package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Component not found in components.json
 */
class ComponentNotFoundException(componentName: String) : GradleException(
        "Component \"$componentName\" not found in components.json"
)