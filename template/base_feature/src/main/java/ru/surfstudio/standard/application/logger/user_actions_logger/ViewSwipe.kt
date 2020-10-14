package ru.tricolor.android.application.logger.user_actions_logger

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.google.gson.annotations.SerializedName
import ru.tricolor.android.application.logger.user_actions_logger.base.BasicSwipe

class ViewSwipe(view: View, swipeDirection: Direction? = null) : BasicSwipe(swipeDirection) {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("text")
    var text: String? = null

    init {
        id = getIdName(view)

        if (id == null) {
            text = getText(view)
        }
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