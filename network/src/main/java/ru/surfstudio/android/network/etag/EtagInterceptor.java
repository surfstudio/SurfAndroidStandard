package ru.surfstudio.android.network.etag;


import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.network.etag.storage.EtagStorage;

import static ru.surfstudio.android.network.ServerConstants.HEADER_QUERY_MODE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_ONLY_IF_CHANGED;
import static ru.surfstudio.android.network.etag.EtagConstants.HEADER_REQUEST_ETAG;
import static ru.surfstudio.android.network.etag.EtagConstants.HEADER_RESPONSE_ETAG;

/**
 * добавляет etag в header запроса и запоминает etag из ответа. Etag получают и сохраняют с помощью
 * EtagStorage. Только если в запросе присутствует header queryMode: ServerConstant.QUERY_MODE_ONLY_IF_CHANGED, в запрос будет
 * добавлен header c etag, header queryMode будет удален.
 */
@PerApplication
public class EtagInterceptor implements Interceptor {

    private final EtagStorage etagStorage;

    @Inject
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
                newHeadersBuilder.add(HEADER_REQUEST_ETAG, outputEtag);
            }
            final Request request = originalRequest.newBuilder()
                    .headers(newHeadersBuilder.build())
                    .build();

            final Response response = chain.proceed(request);

            String inputEtag = response.header(HEADER_RESPONSE_ETAG);
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
