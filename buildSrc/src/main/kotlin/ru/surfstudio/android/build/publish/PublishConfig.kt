package ru.surfstudio.android.build.publish

/**
 * Common configuration for publish gradle task
 */
object PublishConfig {
    const val LICENSE_NAME = "The Apache License, Version 2.0"
    const val LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0"

    const val SCM_CONNECTION = "scm:git:git@github.com:surfstudio/SurfAndroidStandard.git"
    const val SCM_DEVELOPER_CONNECTION = "scm:git:ssh://github.com:surfstudio/SurfAndroidStandard.git"
    const val SCM_URL = "https://github.com/surfstudio/SurfAndroidStandard"

    const val DEVELOPER_NAME = "SurfStudio"
    const val DEVELOPER_MAIL = "mail@surfstudio.ru"
    const val DEVELOPER_ORGANISATION = "SurfStudio LLC"
    const val DEVELOPER_ORGANISATION_URL = "https://surf.ru"

    const val ISSUE_MANAGEMENT_SYSTEM = "GitHub"
    const val ISSUE_MANAGEMENT_URL = "$SCM_URL/issues"
    const val REPOSITORY_NAME = "External"
}