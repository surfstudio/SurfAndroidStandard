package ru.surfstudio.android.build.exceptions.component

import org.gradle.api.GradleException

/**
 * Parsing component.json exception
 */
class ComponentsJsonParsingException : GradleException("Can't parse value.json")