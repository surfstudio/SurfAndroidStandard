package ru.surfstudio.standard.i_network.error.exception

import java.lang.RuntimeException

/**
 * Ошибка если сервер возвращает недопустимые значения
 */
class InvalidServerValuesResponse(vararg values: Pair<String, String>) : RuntimeException(pairToMessage(values))

private fun pairToMessage(values: Array<out Pair<String, String>>): String {
    val joinedValues: String = values.asSequence()
            .joinToString { "\ttype: " + it.first + " value: " + it.second + "\n" }
    return "Недопустимые значения сервера:\n $joinedValues"
}
