package ru.surfstudio.android.picturechooser

import android.net.Uri
import java.io.Serializable

/**
 * Класс-обертка для возвращения Uri с помощью ActivityWithResultRoute
 */
data class UriWrapper(val uri: Uri) : Serializable