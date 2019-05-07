package ru.surfstudio.android.core.mvp.binding.rx.relation.mvp

import java.lang.Exception

/**
 * Пустая ошибка, которую не нужно обрабатывать (аналог null в rx-цепочке)
 */
class EmptyErrorException : Exception()