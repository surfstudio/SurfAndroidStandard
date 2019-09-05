package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Throws if property version doesn't equals component version
 */
class PropertyVersionsNotEqualsComponentVersionException(
        componentName: String,
        componentVersion: String,
        propertyVersion: String
) : GradleException(
        "Component \"$componentName\" has version \"$componentVersion\", but checked version \"$propertyVersion\""
)