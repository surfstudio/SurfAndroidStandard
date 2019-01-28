package ru.surfstudio.android.template.test_utils

import kotlin.annotation.Retention

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class WaitApiTest