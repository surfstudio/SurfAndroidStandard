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
package ru.surfstudio.standard.i_network.network.etag;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.standard.i_network.network.etag.storage.EtagStorage;

import static ru.surfstudio.standard.i_network.network.BaseServerConstants.HEADER_QUERY_MODE;
import static ru.surfstudio.standard.i_network.network.BaseServerConstants.QUERY_MODE_ONLY_IF_CHANGED;

/**
 * добавляет etag в header запроса и запоминает etag из ответа. Etag получают и сохраняют с помощью
 * EtagStorage. Только если в запросе присутствует header queryMode: ServerConstant.QUERY_MODE_ONLY_IF_CHANGED, в запрос будет
 * добавлен header c etag, header queryMode будет удален.
 */
public class EtagInterceptor implements Interceptor {

    private final EtagStorage etagStorage;

    public EtagInterceptor(EtagStorage etagStorage) {
        this.etagStorage = etagStorage;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final HttpUrl url = originalRequest.url();
        final String headerQueryMode = originalRequest.header(HEADER_QUERY_MODE);
        boolean supportEtag = headerQueryMode != null;

        if (supportEtag) {
            Headers.Builder newHeadersBuilder = originalRequest.headers().newBuilder();
            newHeadersBuilder.removeAll(HEADER_QUERY_MODE);
            int queryMode = Integer.parseInt(headerQueryMode);

            if (queryMode == QUERY_MODE_ONLY_IF_CHANGED) {
                String outputEtag = etagStorage.getEtag(url.toString());
                newHeadersBuilder.add(EtagConstants.HEADER_REQUEST_ETAG, outputEtag);
            }
            final Request request = originalRequest.newBuilder()
                    .headers(newHeadersBuilder.build())
                    .build();

            final Response response = chain.proceed(request);

            String inputEtag = response.header(EtagConstants.HEADER_RESPONSE_ETAG);
            if (response.isSuccessful()) {
                if (inputEtag != null) {
                    etagStorage.putEtag(url.toString(), inputEtag);
                } else {
                    Logger.e("missing etag in response, request url: %s", url.toString());
                }
            }
            return response;
        } else {
            return chain.proceed(originalRequest);
        }
    }
}
