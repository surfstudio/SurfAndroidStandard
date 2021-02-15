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

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BaseServerConstants {

    public static final String HEADER_QUERY_MODE = "queryMode";
    public static final int QUERY_MODE_ONLY_IF_CHANGED = 1; //используется механизм etag
    public static final int QUERY_MODE_FORCE = 2; //получить данные с сервера принудительно
    public static final int QUERY_MODE_FROM_SIMPLE_CACHE = 3; //получить данные из кеша

    @IntDef({QUERY_MODE_ONLY_IF_CHANGED,
            QUERY_MODE_FORCE,
            QUERY_MODE_FROM_SIMPLE_CACHE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface QueryMode {
    }
}
