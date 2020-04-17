package ru.surfstudio.android.navigation.route.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.route.BaseRoute
import ru.surfstudio.android.navigation.route.Route.Companion.SCREEN_ID
import ru.surfstudio.android.navigation.route.activity.ActivityRoute.Companion.EXTRA_DATA_BUNDLE

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
                .apply {
                    //TODO подумать, хорошая ли идея класть сюда SCREEN_ID для идентификации экрана
                    putExtra(EXTRA_DATA_BUNDLE, prepareData())
                    putExtra(SCREEN_ID, getTag())
                }
    }
}

/**
 * Extracts data bundle that is created in [BaseRoute.prepareData] from intent.
 */
fun Intent.getDataBundle(): Bundle? = getBundleExtra(EXTRA_DATA_BUNDLE)