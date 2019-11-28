/*
  Copyright (c) 2019-present, SurfStudio LLC. Margarita Volodina.

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
package ru.surfstudio.android.security.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import ru.surfstudio.android.security.crypto.security.SecurityUtils
import ru.surfstudio.android.utilktx.util.SdkUtils

private const val SIGNATURE_HASH_ALGORITHM = "SHA1"

/**
 * Release signature hash.
 *
 * todo replace with a valid value
 *
 * WARNING: Release signature hash may be different after uploading app to Google Play
 * when using Google Play App Signing mechanism.
 *
 * Actual release signature hash could be found at Google developer console
 */
private const val GOOGLE_PLAY_SIGNATURE_HASH = ""

/**
 * Object for checking release signature.
 */
object ReleaseSignatureChecker {

    /**
     * Function for checking if release signature of application is valid
     */
    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    fun isReleaseSignatureValid(context: Context): Boolean {
        if (SdkUtils.isAtLeastPie()) {
            val signingInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
            ).signingInfo
            if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners.forEach {
                    if (checkSignature(it, GOOGLE_PLAY_SIGNATURE_HASH)) {
                        return true
                    }
                }
            } else {
                // Send one with signingCertificateHistory
                signingInfo.signingCertificateHistory.forEach {
                    if (checkSignature(it, GOOGLE_PLAY_SIGNATURE_HASH)) {
                        return true
                    }
                }
            }
        } else {
            val packageInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
            )
            packageInfo.signatures.forEach {
                if (checkSignature(it, GOOGLE_PLAY_SIGNATURE_HASH)) {
                    return true
                }
            }
        }
        return false
    }

    private fun checkSignature(signature: Signature, signatureHash: String): Boolean =
            getSHA1(signature.toByteArray()) == signatureHash

    private fun getSHA1(signature: ByteArray): String =
            with(SecurityUtils) {
                bytesToHex(messageDigest(signature, SIGNATURE_HASH_ALGORITHM))
            }
}