package ru.surfstudio.android.recycler.decorator.sample

import android.content.res.Resources

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()