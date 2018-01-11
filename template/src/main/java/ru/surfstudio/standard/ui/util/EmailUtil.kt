package ru.surfstudio.standard.ui.util


import ru.surfstudio.standard.interactor.common.error.NonInstanceClassCreateException

/**
 * Утилиты для работы с email
 */
class EmailUtil private constructor() {

    init {
        throw NonInstanceClassCreateException(EmailUtil::class.java)
    }

    companion object {

        fun isEmailValid(email: String): Boolean {
            return email.length == 0 || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
