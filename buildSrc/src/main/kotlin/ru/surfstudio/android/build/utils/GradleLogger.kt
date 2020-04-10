package ru.surfstudio.android.build.utils

/** Сущность, выводящая логи в консоль. */
interface GradleLogger {

    var tag: String
    var isDebugEnabled: Boolean
    var isInfoEnabled: Boolean
    var isWarningsEnabled: Boolean
    var isErrorsEnabled: Boolean

    fun logDebug(message: String)
    fun logInfo(message: String)
    fun logWarning(message: String)
    fun logError(message: String)

    fun String.logd() = logDebug(this)
    fun String.logi() = logInfo(this)
    fun String.logw() = logWarning(this)
    fun String.loge() = logError(this)
}

/** Реализация логгера по-умолчанию. */
class DefaultGradleLogger(
        override var tag: String = EMPTY_STRING,
        override var isDebugEnabled: Boolean = true,
        override var isInfoEnabled: Boolean = true,
        override var isWarningsEnabled: Boolean = true,
        override var isErrorsEnabled: Boolean = true
) : GradleLogger {

    override fun logDebug(message: String) {
        if (isDebugEnabled) println("[D] $tag: $message")
    }

    override fun logInfo(message: String) {
        if (isInfoEnabled) println("[I] $tag: $message")
    }

    override fun logWarning(message: String) {
        if (isWarningsEnabled) println("[WARN] $tag: $message")
    }

    override fun logError(message: String) {
        if (isErrorsEnabled) println("\n[ERROR] $tag: $message")
    }
}