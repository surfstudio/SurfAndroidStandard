package ru.surfstudio.android.utilktx.ktx.dataextender

/**
 * Extension-функции для [BaseDataExtender]
 */

fun <T> Collection<BaseDataExtender<T>>.getData(): Collection<T>
        = this.map { it.data }