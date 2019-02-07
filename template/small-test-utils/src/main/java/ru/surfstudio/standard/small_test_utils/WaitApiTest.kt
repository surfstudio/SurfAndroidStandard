package ru.surfstudio.standard.small_test_utils

import kotlin.annotation.Retention
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class WaitApiTest(val checkedException: KClass<out Throwable> = Throwable::class)