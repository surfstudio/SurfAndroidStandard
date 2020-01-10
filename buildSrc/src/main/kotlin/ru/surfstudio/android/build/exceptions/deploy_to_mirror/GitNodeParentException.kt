package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

class GitNodeParentException(nodeString: String) : GradleException(
        "Node: \n$nodeString \nhas wrong parents"
)