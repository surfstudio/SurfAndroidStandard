package ru.surfstudio.standard.i_network.network.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Конфигуратор [Gson] перед его созданием
 */
interface BaseGsonConfigurator {

    fun configureGson(builder: GsonBuilder)
}