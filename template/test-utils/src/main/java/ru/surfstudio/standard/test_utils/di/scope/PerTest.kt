package ru.surfstudio.standard.test_utils.di.scope

import javax.inject.Scope
import kotlin.annotation.Retention

/**
 * Test-level dagger scope
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerTest