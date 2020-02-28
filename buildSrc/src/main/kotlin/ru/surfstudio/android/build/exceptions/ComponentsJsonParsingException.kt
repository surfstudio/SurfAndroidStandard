package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Parsing component.json exception
 */
class ComponentsJsonParsingException : GradleException("Can't parse value.json")