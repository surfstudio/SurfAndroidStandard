package ru.tricolor.android.application.logger.user_actions_logger

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.children
import com.google.gson.annotations.SerializedName
import ru.tricolor.android.application.logger.user_actions_logger.base.BasicInput

class ViewInput(view: EditText): BasicInput() {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("text")
    var text: String? = null

    @SerializedName("entered_text")
    var enteredText: CharSequence? = null

    init {
        id = getIdName(view)

        if (id == null) {
            text = getText(view)
        }

        enteredText = view.editableText.toString()
    }

    private fun getIdName(view: View) =
        try {
            view.resources.getResourceEntryName(view.id)
        } catch (e: Resources.NotFoundException) {
            null
        }

    private fun getText(view: View): String? {
        if (view is TextView) {
            return view.text.toString()
        } else if (view is ViewGroup) {
            return view.children.find { it is TextView }
                ?.let { (it as TextView).text.toString() }
        }
        return null
    }
}