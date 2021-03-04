package ru.surfstudio.android.navigation.sample.app.screen.system

import android.content.Context
import androidx.core.content.FileProvider

class AppFileProvider : FileProvider() {

    companion object {
        private const val PROVIDER_AUTHORITY_SEGMENT = ".app_fileprovider"

        fun getAuthority(context: Context): String {
            return context.packageName + PROVIDER_AUTHORITY_SEGMENT
        }
    }
}
