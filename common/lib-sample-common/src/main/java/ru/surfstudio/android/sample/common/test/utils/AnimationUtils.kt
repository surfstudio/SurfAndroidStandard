/*
  Copyright (c) 2020-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.sample.common.test.utils

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