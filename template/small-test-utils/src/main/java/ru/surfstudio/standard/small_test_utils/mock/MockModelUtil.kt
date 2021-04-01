package ru.surfstudio.standard.small_test_utils.mock

import com.google.gson.Gson
import ru.surfstudio.standard.i_network.network.Transformable
import kotlin.reflect.KClass


/**
 * Parses [json] into domain model
 * @param json representation of model
 * @param objModelClass mapping class for domain model
 */
fun <T, O : Transformable<T>> parseMockModel(json: String, objModelClass: KClass<O>): T {
    val objModel = Gson().fromJson(json, objModelClass.java)
    return objModel.transform()
}
