package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about components check result
 */
data class CheckComponentsConfigurationResult(val isOk: Boolean, val reasonFail: String = EMPTY_STRING) {
}