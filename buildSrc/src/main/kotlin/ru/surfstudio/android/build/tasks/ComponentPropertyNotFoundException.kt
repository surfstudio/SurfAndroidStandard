package ru.surfstudio.android.build.tasks

import org.gradle.api.GradleException

/**
 * Component property not found
 */
class ComponentPropertyNotFoundException : GradleException(
        "Component property doesn't found. Please define : -Pcomponent=<component_name>"
)