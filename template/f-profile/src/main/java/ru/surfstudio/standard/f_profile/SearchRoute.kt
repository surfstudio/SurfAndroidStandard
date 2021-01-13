package ru.surfstudio.standard.f_profile

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import ru.surfstudio.android.navigation.route.result.ActivityResultRoute
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

class SearchRoute(override val screenId: String) : ActivityResultRoute<String>() {

    override fun createIntent(context: Context): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    override fun parseResultIntent(resultIntent: Intent?): String {
        val results: List<String> = resultIntent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?: emptyList()
        return if (results.isNotEmpty()) results[0] else EMPTY_STRING
    }

    override fun getId(): String {
        return "${SearchRoute::class.java.canonicalName}${screenId.hashCode()}"
    }
}