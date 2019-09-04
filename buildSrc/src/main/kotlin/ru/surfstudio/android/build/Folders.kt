package ru.surfstudio.android.build

object Folders {

    const val BUILD_FOLDER_NAME = "build"
    private const val OUTPUT_FOLDER_NAME = "outputs"
    private const val CHECK_STABLE_COMPONENTS_CHANGED_FOLDER_NAME = "check-stable-components-changed-task"
    const val TEMP_FOLDER_NAME = "temp"
    const val BUILD_OUTPUT_FOLDER_PATH = "$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME"
    const val OUTPUT_JSON_FOLDER_PATH = "$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME/$CHECK_STABLE_COMPONENTS_CHANGED_FOLDER_NAME/"
    const val CONFIG_GRADLE_PATH = "/buildSrc/config.gradle"
}