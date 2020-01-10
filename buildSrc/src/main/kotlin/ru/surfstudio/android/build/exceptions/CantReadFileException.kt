package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

class CantReadFileException(fileName: String) : GradleException(
        "Can`t read file $fileName"
)