package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Sample directory doesn't exist
 */
class SampleDirectoryNotExistException(
        componentName: String,
        sampleName: String
) : GradleException(
        "Component $componentName has sample $sampleName, but folder doesn't exist."
)