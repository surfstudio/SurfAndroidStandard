package ru.surfstudio.android.core.mvp.binding.react.loadable.data

import java.lang.Exception

interface Error {
    val cause: Throwable
}

class EmptyErrorException : Exception()
class EmptyError(override val cause: Throwable = EmptyErrorException()) : Error

class CommonError(override val cause: Throwable) : Error