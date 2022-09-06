/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.utilktx.util


import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Утилиты для проверки версии Api
 */
object SdkUtils {

    fun isPreLollipop(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
    fun isAtLeastLollipop(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    fun isAtLeastMarshmallow(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    fun isAtLeastNougat(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    fun isAtLeastOreo(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    fun isAtLeastPie(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun isAtLeastQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    fun isAtLeastR(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun isAtLeastS(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun isAtLeastTiramisu(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    /**
     * Запускает блок кода на устройствах с андроид версии KitKat и ниже
     *
     * @param block запускаемый блок кода
     */
    fun runOnPreLollipop(block: () -> Unit) {
        if (isPreLollipop()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии Lollipop и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnLollipop(block: () -> Unit) {
        if (isAtLeastLollipop()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии Marshmallow и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnMarshmallow(block: () -> Unit) {
        if (isAtLeastMarshmallow()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии Noughat и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnNoughat(block: () -> Unit) {
        if (isAtLeastNougat()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии Oreo и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnOreo(block: () -> Unit) {
        if (isAtLeastOreo()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии Pie и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnPie(block: () -> Unit) {
        if (isAtLeastPie()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии Q и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnQ(block: () -> Unit) {
        if (isAtLeastQ()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии R и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnR(block: () -> Unit) {
        if (isAtLeastR()) {
            block()
        }
    }

    /**
     * Запускает блок кода на устройствах с андроид версии S и выше
     *
     * @param block запускаемый блок кода
     */
    fun runOnS(block: () -> Unit) {
        if (isAtLeastS()) {
            block()
        }
    }

    /**
     * Запускает ту, или иную лямбду в зависимости какая версия api на устройстве
     */
    fun doIfSdk(atLeast: Boolean, ifTrue: () -> Unit, ifFalse: () -> Unit) =
        if (atLeast) ifTrue() else ifFalse()
}
