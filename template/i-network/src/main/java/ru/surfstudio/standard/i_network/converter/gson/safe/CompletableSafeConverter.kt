package ru.surfstudio.standard.i_network.converter.gson.safe

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable

/**
 * SafeConverter for [Completable].
 *
 * We don't expect from it any data, so just return [Completable.complete].
 * */
class CompletableSafeConverter(type: TypeToken<Completable>) : SafeConverter<Completable>(type) {
    override fun convert(
            typeAdapterFactory: TypeAdapterFactory?,
            gson: Gson?,
            element: JsonElement?
    ): Completable {
        return Completable.complete()
    }
}