package ru.surfstudio.android.build.utils

import groovy.lang.MissingPropertyException
import org.gradle.api.Project
import org.gradle.api.UnknownProjectException
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.exceptions.component.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.component.ComponentPropertyNotFoundException
import ru.surfstudio.android.build.exceptions.property.PropertyNotDefineException
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

/**
 * Read property from Project without Exception
 */
fun <T> Project.readProperty(name: String, defValue: T): T {
    try {
        return property(name) as? T ?: defValue
    } catch (e: MissingPropertyException) {
        // Missing property
    } catch (e: UnknownProjectException) {
        // Missing property
    }
    return defValue
}

/**
 * Extract property from project
 *
 * @throws PropertyNotDefineException if property doesn't define
 */
fun Project.extractProperty(propertyName: String): String {
    return property(propertyName) as? String ?: throw PropertyNotDefineException(propertyName)
}