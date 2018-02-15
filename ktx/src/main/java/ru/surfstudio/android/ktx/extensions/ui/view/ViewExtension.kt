package ru.surfstudio.android.ktx.extensions.ui.view

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.annotation.ColorInt
import android.view.View

/**
 * Extension-методы для View
 */


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

fun View.invisibleIf(invisible: Boolean) {
    if (invisible) {
        visibility = View.VISIBLE
        isEnabled = true
    } else {
        visibility = View.INVISIBLE
        isEnabled = false
    }
}

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
 * Extension метод, который запускает action если данные изменились
 */
fun <T : View, R> T.actionIfChanged(data: R?, action: T.(data: R?) -> Unit) {
    this.actionIfChanged(data, null,null,null,  {d,_,_,_ -> action(d)})
}

fun <T : View, R1,R2> T.actionIfChanged(data1: R1?,data2: R2?, action: T.(R1?,R2?) -> Unit) {
    this.actionIfChanged(data1, data2,null,null,  {d1,d2,_,_ -> action(d1,d2)})
}

fun <T : View, R1,R2,R3> T.actionIfChanged(data1: R1?,data2: R2?, data3: R3? = null, action: T.(R1?,R2?,R3?) -> Unit) {
    this.actionIfChanged(data1, data2,data3,null,  {d1,d2,d3,_ -> action(d1,d2,d3)})
}

fun <T : View, R1, R2, R3, R4> T.actionIfChanged(
        data1: R1, data2: R2? = null, data3: R3? = null, data4: R4? = null,
        action: T.(data1: R1?, data2: R2?, data3: R3?, data4: R4?) -> Unit) {
    val hash = data1?.hashCode()
            ?.plus(data2?.hashCode() ?: 0)
            ?.plus(data3?.hashCode() ?: 0)
            ?.plus(data4?.hashCode() ?: 0)
    if (this.tag != hash) {
        action(data1,data2, data3, data4)
        this.tag = hash
    }
}