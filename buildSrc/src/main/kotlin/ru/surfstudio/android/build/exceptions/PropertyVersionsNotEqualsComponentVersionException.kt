package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException


class PropertyVersionsNotEqualsComponentVersionException(
        componentName: String,
        componentVersion: String,
        propertyVersion: String
) : GradleException(
        "Component \"$componentName\" has version \"$componentVersion\", but checked version \"$propertyVersion\""
)