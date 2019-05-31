package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING

enum class ComponentChangeReason(val reason: String) {
    NO_REASON(EMPTY_STRING),
    FILE_CHANGED("files in revisions are different"),
    COMPONENT_REMOVED("No information about component in revision to compare:"),
    LIBRARIES_DIFFER("Libraries lists are different"),
    GENERAL_VALUES_DIFFER("One of the following values differ: libMinSdkVersion,targetSdkVersion," +
            "compileSdkVersion, moduleVersionCode")
}