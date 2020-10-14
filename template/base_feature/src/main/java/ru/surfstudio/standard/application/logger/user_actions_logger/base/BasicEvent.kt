package ru.tricolor.android.application.logger.user_actions_logger.base

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Класс базового пользовательского действия
 *
 * Используется как маркер и нужен для инкапсуляции gson
 *
 * @param gesture - тип логгируемого жеств
 */
open class BasicEvent(
    @SerializedName("gesture")
    private val gesture: String
) {

    @Transient
    private val gson = Gson()

    override fun toString(): String = gson.toJson(this)
}