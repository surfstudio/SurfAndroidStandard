package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

/**
 * Base exception class for mirror deploy
 */
abstract class BaseDeployToMirrorException(
        repositoryName: String,
        message: String
) : GradleException(
        "\"$repositoryName\" repository error : $message"
)