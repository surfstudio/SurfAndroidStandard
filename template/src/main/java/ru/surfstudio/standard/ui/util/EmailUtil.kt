package ru.surfstudio.standard.ui.util

/**
 * Утилиты для работы с email
 */
class EmailUtil private constructor() {

    companion object {

        fun isEmailValid(email: String): Boolean {
            return email.length == 0 || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
