package ru.surfstudio.standard.base.test.mock

import com.google.gson.Gson
import kotlin.reflect.KClass


object ModelParser {

    /**
     * Parses [json] into model
     * @param json representation of model
     * @param modelClass model class
     */
    fun <T : Any> parse(json: String, modelClass: KClass<T>): T {
        return Gson().fromJson(json, modelClass.java)
    }
}
