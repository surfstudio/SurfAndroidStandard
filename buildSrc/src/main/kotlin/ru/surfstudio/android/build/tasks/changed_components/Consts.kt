package ru.surfstudio.android.build.tasks.changed_components

//task params
const val PATH_TO_FILE = "pathToFile"
const val REVISION = "revision"
const val REVISION_TO_COMPARE = "revisionToCompare"

//task names
const val GRADLE_TASK_CREATE_PROJECT_CONFIGURATION = "CreateProjectConfiguration"
const val GRADLE_TASK_CREATE_FROM_TEMP = "runCreateProjectConfigurationFromTemp"
const val CHECK_STABLE_COMPONENTS_TASK_NAME = "CheckStableComponentsChanged "
const val INCREMENT_UNSTABLE_CHANGED_TASK_NAME = "IncrementUnstableChangedComponentsTask "

//folders
const val OUTPUT_FOLDER_NAME = "outputs"
const val BUILD_FOLDER_NAME = "build"
const val CURRENT_TASK_FOLDER_NAME = "check-stable-components-changed-task"
const val TEMP_FOLDER_NAME = "temp"
const val BUILD_OUTPUT_FOLDER_PATH = "$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME"
const val OUTPUT_JSON_FOLDER_PATH = "$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME/$CURRENT_TASK_FOLDER_NAME/"
const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"