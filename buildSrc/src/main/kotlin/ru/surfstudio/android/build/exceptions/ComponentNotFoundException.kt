package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Component not found in Components
 */
class ComponentNotFoundException : GradleException(
        "Component not found in components.json"
)