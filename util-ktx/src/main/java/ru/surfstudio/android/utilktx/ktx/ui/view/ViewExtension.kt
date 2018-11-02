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
package ru.surfstudio.android.utilktx.ktx.ui.view

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorInt
import android.view.View
import ru.surfstudio.android.utilktx.util.KeyboardUtil
import android.view.ViewGroup.MarginLayoutParams

/**
 * Extension-методы для View.
 */

/**
 * Установка отступа от низа View.
 *
 * @param bottomMarginPx величина отступа в px.
 */
fun View.setBottomMargin(bottomMarginPx: Int) {
    val params = layoutParams as? MarginLayoutParams
    params?.bottomMargin = bottomMarginPx
}

/**
 * Установка отступа от верха View.
 *
 * @param topMarginPx величина отступа в px.
 */
fun View.setTopMargin(topMarginPx: Int) {
    val params = layoutParams as? MarginLayoutParams
    params?.topMargin = topMarginPx
}

/**
 * Установка отступа слева от View.
 *
 * @param leftMarginPx величина отступа в px.
 */
fun View.setLeftMargin(leftMarginPx: Int) {
    val params = layoutParams as? MarginLayoutParams
    params?.leftMargin = leftMarginPx
}

/**
 * Установка отступа справа от View.
 *
 * @param rightMarginPx величина отступа в px.
 */
fun View.setRightMargin(rightMarginPx: Int) {
    val params = layoutParams as? MarginLayoutParams
    params?.rightMargin = rightMarginPx
}

/**
 * Меняет цвет background у view
 */
fun View.changeViewBackgroundColor(@ColorInt color: Int) {
    val background = background.mutate()
    when (background) {
        is ShapeDrawable -> background.paint.color = color
        is GradientDrawable -> background.setColor(color)
        is ColorDrawable -> background.color = color
    }
}

/**
 * Делает вью невидимой и задизейбленной, если выполняется условие
 */
fun View.invisibleIf(invisible: Boolean) {
    if (invisible) {
        visibility = View.INVISIBLE
        isEnabled = false
    } else {
        visibility = View.VISIBLE
        isEnabled = true
    }
}

/**
 * Cкрывает вью , если выполняется условие
 */
fun View.goneIf(gone: Boolean) {
    if (gone) {
        visibility = View.GONE
        isEnabled = false
    } else {
        visibility = View.VISIBLE
        isEnabled = true
    }
}

/**
 * Убирает экранную клавиатуру
 */
fun View.hideSoftKeyboard() {
    KeyboardUtil.hideSoftKeyboard(this)
}


//==================== ACTION IF CHANGED =================================

/**
 * Extension метод, который запускает action если данные изменились
 */
fun <T : View, R> T.actionIfChanged(data: R?, action: T.(data: R?) -> Unit) {
    this.actionIfChanged(data, null, null, null, { d, _, _, _ -> action(d) })
}

fun <T : View, R1, R2> T.actionIfChanged(data1: R1?, data2: R2?, action: T.(R1?, R2?) -> Unit) {
    this.actionIfChanged(data1, data2, null, null, { d1, d2, _, _ -> action(d1, d2) })
}

fun <T : View, R1, R2, R3> T.actionIfChanged(data1: R1?, data2: R2?, data3: R3? = null, action: T.(R1?, R2?, R3?) -> Unit) {
    this.actionIfChanged(data1, data2, data3, null, { d1, d2, d3, _ -> action(d1, d2, d3) })
}

fun <T : View, R1, R2, R3, R4> T.actionIfChanged(
        data1: R1?,
        data2: R2? = null,
        data3: R3? = null,
        data4: R4? = null,
        action: T.(data1: R1?, data2: R2?, data3: R3?, data4: R4?) -> Unit) {
    val hash = data1?.hashCode()
            ?.plus(data2?.hashCode() ?: 0)
            ?.plus(data3?.hashCode() ?: 0)
            ?.plus(data4?.hashCode() ?: 0)
    if (this.tag != hash) {
        action(data1, data2, data3, data4)
        this.tag = hash
    }
}