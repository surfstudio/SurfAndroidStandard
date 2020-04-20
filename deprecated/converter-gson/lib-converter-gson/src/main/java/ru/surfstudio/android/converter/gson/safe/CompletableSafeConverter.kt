/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.converter.gson.safe

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
@Deprecated(message = "empty")
class CompletableSafeConverter(type: TypeToken<Completable>) : SafeConverter<Completable>(type) {
    override fun convert(
            typeAdapterFactory: TypeAdapterFactory?,
            gson: Gson?,
            element: JsonElement?
    ): Completable {
        return Completable.complete()
    }
}