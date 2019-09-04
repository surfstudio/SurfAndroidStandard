package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

class ComponentUnstableException(componentName: String) : GradleException(
        "Component \"$componentName\" is unstable"
)