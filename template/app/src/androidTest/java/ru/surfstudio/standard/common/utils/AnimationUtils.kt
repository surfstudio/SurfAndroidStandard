package ru.surfstudio.standard.common.utils

/**
 * Утилиты для настройки анимаций во время выполнения инструментальных тестов
 */
object AnimationUtils {

    private const val animationPermission = "android.permission.SET_ANIMATION_SCALE"

    private val disableAnimationsCommands = arrayOf(
            "settings put global window_animation_scale 0",
            "settings put global transition_animation_scale 0",
            "settings put global animator_duration_scale 0"
    )

    private val enableAnimationsCommands = arrayOf(
            "settings put global window_animation_scale 1",
            "settings put global transition_animation_scale 1",
            "settings put global animator_duration_scale 1"
    )

    fun grantScaleAnimationPermission() {
        PermissionUtils.grantPermissions(animationPermission)
    }

    fun disableAnimations() {
        ShellUtils.executeCommands(*disableAnimationsCommands)
    }

    fun enableAnimations() {
        ShellUtils.executeCommands(*enableAnimationsCommands)
    }
}