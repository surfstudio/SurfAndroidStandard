package ru.surfstudio.android.security.connection.ssl

import android.content.Context
import android.support.annotation.RawRes
import android.util.Base64
import okhttp3.CertificatePinner
import ru.surfstudio.android.dagger.scope.PerApplication
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.inject.Inject

/**
 * Класс, создающий CertificatePinner для OkHttpClient для реализации ssl-pinning.
 * Проверяется только сертификат сайта.
 */
@PerApplication
class CertificatePinnerCreator @Inject constructor(private val context: Context) {

    companion object {
        private const val certificateType = "X509"
        private const val algorithmType = "SHA-256"
        private const val certificatePrefix = "sha256/"
    }

    fun create(@RawRes certId: Int, hostName: String): CertificatePinner {
        return CertificatePinner.Builder()
                .add(hostName, extractPeerCertificate(context.resources.openRawResource(certId)))
                .build()
    }

    /**
     * Get peer certificate (Public key to sha256 to base64)
     * @param certificateInputStream InputStream from Crt or der or pem file with a valid certificate
     */
    private fun extractPeerCertificate(certificateInputStream: InputStream?): String {
        try {
            val x509Certificate = CertificateFactory.getInstance(certificateType)
                    .generateCertificate(certificateInputStream) as X509Certificate

            val publicKeyEncoded = x509Certificate.publicKey.encoded
            val messageDigest = MessageDigest.getInstance(algorithmType)
            val publicKeySha256 = messageDigest.digest(publicKeyEncoded)
            val publicKeyShaBase64 = Base64.encode(publicKeySha256, Base64.NO_WRAP)

            return certificatePrefix + publicKeyShaBase64
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                certificateInputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }
}