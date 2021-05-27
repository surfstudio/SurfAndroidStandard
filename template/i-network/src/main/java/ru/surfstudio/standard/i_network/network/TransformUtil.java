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
package ru.surfstudio.standard.i_network.network;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.List;

import ru.surfstudio.android.utilktx.util.java.CollectionUtils;


/**
 * содержит методы для трансформации объектов Domain слоя в данные для сервера и наоборот.
 * Упрощает работу с {@link Transformable}
 */
public final class TransformUtil {


    private TransformUtil() {
        throw new IllegalStateException("no instance allowed");
    }

    public static <T, E extends Transformable<T>> T transform(@Nullable E object) {
        return object != null ? object.transform() : null;
    }

    public static <T, E extends Transformable<T>> List<T> transformCollection(Collection<E> src) {
        return CollectionUtils.mapEmptyIfNull(src, Transformable::transform);
    }

    public static <T, E> List<T> transformCollection(Collection<E> src, final CollectionUtils.Mapper<E, T> mapper) {
        return CollectionUtils.mapEmptyIfNull(src, mapper);
    }
}
