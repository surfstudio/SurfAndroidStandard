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
package ru.surfstudio.standard.i_network.network.error;


/**
 * получен ответ не 2xx
 */
public abstract class HttpError extends NetworkException {
    private final int code;

    public HttpError(Throwable cause, int code, String url) {
        super(prepareMessage(code, url, 0), cause);
        this.code = code;
    }

    private static String prepareMessage(int code, String url, int innerCode) {
        return " httpCode=" + code + "\n" +
                ", url='" + url + "'" + "\n" +
                ", innerCode=" + innerCode;
    }

    public int getCode() {
        return code;
    }
}
