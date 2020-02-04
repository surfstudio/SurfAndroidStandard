package ru.surfstudio.navigation.route.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.navigation.route.activity.ActivityRoute.Companion.EXTRA_DATA_BUNDLE
import androidx.core.app.ActivityOptionsCompat
import ru.surfstudio.navigation.route.BaseRoute

/**
 * Route for [AppCompatActivity].
 */
open class ActivityRoute : BaseRoute<AppCompatActivity>() {

    companion object {
        const val EXTRA_DATA_BUNDLE = "extra data bundle"
    }

    /**
     * Creates the intent to launch Activity with [getScreenClass] or [getScreenClassPath] class.
     *
     * You should override this method if you need custom behavior (system intents, choosers, etc)
     */
    open fun createIntent(context: Context): Intent {
        return Intent(context, requireScreenClass())
                .apply { putExtra(EXTRA_DATA_BUNDLE, prepareData()) }
    }

    /**
     * Prepare options [ActivityOptionsCompat] to launch activity.
     *
     * Can be used to set custom animations, shared element transitions, etc
     */
    fun prepareOptions(): Bundle? = null
}

/**
 * Extracts data bundle that is created in [BaseRoute.prepareData] from intent.
 */
fun Intent.getDataBundle(): Bundle? = getBundleExtra(EXTRA_DATA_BUNDLE)