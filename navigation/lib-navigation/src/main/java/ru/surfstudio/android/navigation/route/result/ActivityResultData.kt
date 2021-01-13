package ru.surfstudio.android.navigation.route.result

import java.io.Serializable

/**
 * Activity execution result
 */
data class ActivityResultData<T : Serializable>(val isSuccess: Boolean, val data: T) : Serializable