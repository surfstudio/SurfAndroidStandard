package ru.surfstudio.android.security.connection.tls

import dagger.Module
import dagger.Provides
import okhttp3.ConnectionSpec
import ru.surfstudio.android.dagger.scope.PerApplication

/**
 * Модуль, поставляющий ConnectionSpec с использованием TLS,
 * который может использоваться в OkHttpClient.Builder()
 * для поддержки сертификатов на Android ниже 5.0
 */
@Module
class ConnectionSpecModule {

    @Provides
    @PerApplication
    internal fun provideTlsConnectionSpec(): ConnectionSpec {
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .allEnabledTlsVersions()
                .allEnabledCipherSuites()
                .build()
    }
}