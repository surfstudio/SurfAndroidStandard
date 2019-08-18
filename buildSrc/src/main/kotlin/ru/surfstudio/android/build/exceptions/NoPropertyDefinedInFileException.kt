package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

class NoPropertyDefinedInFileException(
        propertyName: String,
        fileName: String
) : GradleException(
        "No property $propertyName found in file $fileName"
)
