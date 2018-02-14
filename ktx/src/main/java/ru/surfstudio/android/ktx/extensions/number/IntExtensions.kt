package ru.surfstudio.android.ktx.extensions.number

/**
 * Extension-функции для работы с целыми числами
 */

fun Int.isBetween(int1: Int, int2: Int) = this in (int1 + 1)..(int2 - 1)