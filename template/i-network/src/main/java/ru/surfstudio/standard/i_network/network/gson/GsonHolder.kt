package ru.surfstudio.standard.i_network.network.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Объект ответственный за хранение [Gson]
 */
object GsonHolder {

    val gson: Gson by lazy {
        val builder = GsonBuilder()
        configurator?.configureGson(builder)
        builder.create()
    }

    var configurator: BaseGsonConfigurator? = null
}
