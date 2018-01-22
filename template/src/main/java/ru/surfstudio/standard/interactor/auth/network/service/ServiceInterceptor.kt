package ru.surfstudio.standard.interactor.auth.network.service

import okhttp3.Interceptor
import okhttp3.Response
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.standard.interactor.auth.TokenStorage
import java.io.IOException
import javax.inject.Inject

const val HEADER_AUTH_VALUE = "Bearer %s"
const val HEADER_AUTH_KEY = "Authorization"

/**
 * добавляет необходимые для каждого запроса параметры, такие как token
 */
@PerApplication
class ServiceInterceptor
@Inject
constructor(private val tokenStorage: TokenStorage) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.headers().get(HEADER_AUTH_KEY) != null) {
            return chain.proceed(originalRequest)
        }

        val headersBuilder = originalRequest.headers().newBuilder()
        headersBuilder.add(HEADER_AUTH_KEY, String.format(HEADER_AUTH_VALUE, tokenStorage.authToken))
        val request = originalRequest.newBuilder()
                .headers(headersBuilder.build())
                .build()
        return chain.proceed(request)

    }
}
