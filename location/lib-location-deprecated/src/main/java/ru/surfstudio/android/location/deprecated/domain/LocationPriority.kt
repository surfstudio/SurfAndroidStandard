/*
 * Copyright 2016 Valeriy Shtaits.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.location.deprecated.domain

import androidx.annotation.RequiresPermission

/**
 * Приоритет при определении местоположения.
 */
@Deprecated("Prefer using new implementation")
enum class LocationPriority {

    /**
     * Обеспечивает максимально точное местоположение, которое рассчитывается с использованием как можно большего
     * количества источников (GPS, Wi-Fi, сотовые башни, а также различные датчики). Может привести к значительному
     * истощению батареи.
     */
    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    HIGH_ACCURACY,

    /**
     * Обеспечивает точное местоположение при оптимизации мощности. Очень редко используется GPS. Как правило, для
     * вычисления местоположения устройства используется комбинация Wi-Fi и сотовых башен (~100м).
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"]
    )
    BALANCED_POWER_ACCURACY,

    /**
     * В значительной степени полагается на сотовые башни и избегает GPS и Wi-Fi, обеспечивая грубую (городскую)
     * точность (~10км). Малый разряд батареи.
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"]
    )
    LOW_POWER,

    /**
     * Получение местоположения, когда какое-то другое приложение его запрашивает. Незначительное влияние на заряд
     * батареи.
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"]
    )
    NO_POWER;

    companion object : Comparator<LocationPriority> {

        override fun compare(o1: LocationPriority?, o2: LocationPriority?): Int =
                locationPriorityToCompareWeight(o1) - locationPriorityToCompareWeight(o2)

        private fun locationPriorityToCompareWeight(locationPriority: LocationPriority?) = when(locationPriority) {
            HIGH_ACCURACY -> 3
            BALANCED_POWER_ACCURACY -> 2
            LOW_POWER -> 1
            else -> 0
        }
    }
}