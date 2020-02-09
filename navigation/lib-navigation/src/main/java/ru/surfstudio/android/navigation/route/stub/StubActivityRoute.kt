package ru.surfstudio.android.navigation.route.stub

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Stub route just for executing activity command.
 *
 * Does not contain any information about activity, data or class, and simply used to indicate
 * that particular command should be executed with activity screen.
 */
object StubActivityRoute : ActivityRoute() {

    override fun createIntent(context: Context): Intent = Intent()
}
