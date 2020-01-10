package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Component directory doesn't exist exception
 */
class ComponentDirectoryNotExistException(componentId: String) : GradleException(
        "Component $componentId doesn't have existing directory. " +
                "Please, check value.json and create folder with 'dir' name."
)