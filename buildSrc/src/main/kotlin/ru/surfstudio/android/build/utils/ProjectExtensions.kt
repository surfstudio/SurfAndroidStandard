package ru.surfstudio.android.build.utils

import org.gradle.api.Project
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.exceptions.component.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.component.ComponentPropertyNotFoundException
import ru.surfstudio.android.build.model.Component

/**
 * Component that provided by gradle property
 */
fun Project.getPropertyComponent(): Component {
    val componentName = project.property(GradleProperties.COMPONENT) as? String
            ?: throw ComponentPropertyNotFoundException()
    return Components.value.find { it.name == componentName }
            ?: throw ComponentNotFoundException(componentName)
}