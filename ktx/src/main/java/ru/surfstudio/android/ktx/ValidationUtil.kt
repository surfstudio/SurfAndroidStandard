package ru.surfstudio.android.ktx

import ru.surfstudio.android.logger.Logger

/**
 * Утилита для валидации
 */
object ValidationUtil {

    /**
     * Валидация текста на наличие только латиницы и кириллицы
     */
    fun isTextOnly(fieldText: CharSequence): Boolean =
            fieldText.isNotEmpty() &&
                    fieldText.isNotBlank() &&
                    fieldText.matches("[a-zA-Zа-яА-я]+".toRegex())

    /**
     * Проверяет email на соответсвие требованиям
     * @see <href>http://akkaparallel.blogspot.ru/2013/06/email.html</href>
     */
    fun isEmailValid(emailToCheck: CharSequence): Boolean {
        val regex = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])".toRegex()
        Logger.i("Email validation  $emailToCheck ${emailToCheck.matches(regex)}")
        return emailToCheck.isNotBlank() && emailToCheck.matches(regex)
    }
}