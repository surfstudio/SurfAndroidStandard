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

/**
 * что сделать в первую очередь,
 * вытянуть данные из кеша или попытаться загрузить свежие
 */
public enum DataStrategy {
    CACHE, //Сначала эмитим данные из кеша, если они есть, и потом лезем на сервер
    SERVER, //Сначала проверяем не изменилось ли данные на сервере, если не изменилось или произошла ошибка, то эмитим данные из кеша
    ONLY_ACTUAL, //Эмитим только актуальные данные
    AUTO; //в зависимости от скорости соединения работает либо как CACHE(медленный интернет) или как SERVER

    public static DataStrategy DEFAULT_DATA_STRATEGY = SERVER;

}