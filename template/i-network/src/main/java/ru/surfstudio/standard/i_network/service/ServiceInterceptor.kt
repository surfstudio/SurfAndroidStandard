package ru.surfstudio.standard.i_network.service

import ru.surfstudio.standard.i_token.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import ru.surfstudio.android.dagger.scope.PerApplication
import java.io.IOException
import javax.inject.Inject

const val HEADER_AUTH_KEY = "Bearer"

/**
 * добавляет необходимые для каждого запроса параметры, такие как token
 */
@PerApplication
class ServiceInterceptor @Inject constructor(private val tokenStorage: TokenStorage) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.headers().get(HEADER_AUTH_KEY) != null) {
            return chain.proceed(originalRequest)
        }

        val headersBuilder = originalRequest.headers().newBuilder()
                .add(HEADER_AUTH_KEY, tokenStorage.token)

        val request = originalRequest.newBuilder()
                .headers(headersBuilder.build())
                .build()
        return chain.proceed(request)
    }
}
