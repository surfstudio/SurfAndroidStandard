package ru.tricolor.android.application.logger.user_actions_logger.aspects

import timber.log.Timber

/**
 * Base aspect for logging
 */
abstract class BaseAspect {

    fun log(message: String) {
        Timber.tag(TAG)
        Timber.d(message)
    }

    companion object {
        private const val TAG = "ViewEvent"
    }
}