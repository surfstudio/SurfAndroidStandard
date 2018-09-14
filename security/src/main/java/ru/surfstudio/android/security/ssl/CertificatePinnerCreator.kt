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
package ru.surfstudio.android.security.ssl

import android.content.Context
import android.support.annotation.RawRes
import android.util.Base64
import okhttp3.CertificatePinner
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * Класс, создающий CertificatePinner для OkHttpClient для реализации ssl-pinning.
 * Проверяется только сертификат сайта.
 */
class CertificatePinnerCreator(private val context: Context) {

    companion object {
        private const val certificateType = "X509"
        private const val algorithmType = "SHA-256"
        private const val certificatePrefix = "sha256/"
    }

    fun create(@RawRes certId: Int, hostName: String): CertificatePinner {
        return CertificatePinner.Builder()
                .add(hostName, extractPeerCertificate(certId))
                .build()
    }

    /**
     * Get peer certificate (Public key to sha256 to base64)
     * @param certId certificate ID
     */
    private fun extractPeerCertificate(@RawRes certId: Int): String {
        context.resources.openRawResource(certId).use { certificateInputStream ->
            val x509Certificate = CertificateFactory.getInstance(certificateType)
                    .generateCertificate(certificateInputStream) as X509Certificate

            val publicKeyEncoded = x509Certificate.publicKey.encoded
            val messageDigest = MessageDigest.getInstance(algorithmType)
            val publicKeySha256 = messageDigest.digest(publicKeyEncoded)
            val publicKeyShaBase64 = Base64.encode(publicKeySha256, Base64.NO_WRAP)

            return certificatePrefix + publicKeyShaBase64
        }
    }
}