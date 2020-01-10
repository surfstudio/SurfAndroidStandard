package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.scope

import javax.inject.Scope

/**
 * Кастосный скоуп со временем жизни на две активити
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerLogin