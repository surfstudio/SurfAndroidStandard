package ru.surfstudio.standard.application.logger.user_actions_logger

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.google.gson.Gson

class ViewClick(view: View) {

    var id: String? = null
    var text: String? = null
    var x: Int
    var y: Int

    @Transient
    private val gson = Gson()

    init {
        val locationOnScreen = IntArray(2).also(view::getLocationOnScreen)
        x = locationOnScreen[0]
        y = locationOnScreen[1]

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

    override fun toString(): String = gson.toJson(this)
}