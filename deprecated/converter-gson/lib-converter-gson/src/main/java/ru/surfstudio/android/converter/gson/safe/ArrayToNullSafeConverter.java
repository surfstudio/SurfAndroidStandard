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
package ru.surfstudio.android.converter.gson.safe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;


/**
 * если вместо ожидаемого объекта находится JsonArray - возвращает null
 *
 * @param <T>
 */
@Deprecated
public class ArrayToNullSafeConverter<T> extends SafeConverter<T> {

    public ArrayToNullSafeConverter(TypeToken<T> type) {
        super(type);
    }

    @Override
    public T convert(TypeAdapterFactory typeAdapterFactory, Gson gson, JsonElement element) {
        if (element.isJsonArray()) {
            return null;
        } else {
            return gson.getDelegateAdapter(typeAdapterFactory, getType()).fromJsonTree(element);
        }
    }
}
