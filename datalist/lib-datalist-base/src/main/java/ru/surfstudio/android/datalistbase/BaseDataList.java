/*
  Copyright (c) 2020-present, SurfStudio LLC.

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
package ru.surfstudio.android.datalistbase;

import java.io.Serializable;
import java.util.List;

/**
 * Базовый интерфейс для Datalist'ов
 */
public interface BaseDataList<T> extends List<T>, Serializable {

    @FunctionalInterface
    public interface MapFunc<R, T> {
        R call(T item);
    }

    /**
     * Слияние двух DataList
     *
     * @param data DataList для слияния с текущим
     * @return текущий экземпляр
     */
    public BaseDataList<T> merge(BaseDataList<T> data);

    /**
     * Слияние двух DataList с удалением дублируемых элементов
     * При удалении остаются актуальные (последние присланные сервером) элементы
     *
     * @param data              DataList для слияния с текущим
     * @param distinctPredicate предикат, по которому происходит удаление дублируемых элементов
     * @return текущий экземпляр
     */
    public <R> BaseDataList<T> merge(BaseDataList<T> data, MapFunc<R, T> distinctPredicate);

    /**
     * Преобразует dataList одного типа в dataList другого типа
     *
     * @param mapFunc функция преобразования
     * @param <R>     тип данных нового списка
     * @return DataList с элементами типа R
     */
    public <R> BaseDataList<R> transform(MapFunc<R, T> mapFunc);

    /**
     * Проверка возможности дозагрузки данных
     *
     * @return естьли продолжение листа
     */
    public boolean canGetMore();
}
