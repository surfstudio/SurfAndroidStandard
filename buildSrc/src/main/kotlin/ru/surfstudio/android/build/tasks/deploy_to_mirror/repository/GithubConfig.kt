package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import ru.surfstudio.android.build.utils.EMPTY_STRING

object GithubConfig {
    val USERNAME = System.getenv("surf_github_username") ?: EMPTY_STRING
    val PASSWORD = System.getenv("surf_github_password") ?: EMPTY_STRING
}