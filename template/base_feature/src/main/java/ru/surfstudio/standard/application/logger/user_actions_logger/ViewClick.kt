package ru.tricolor.android.application.logger.user_actions_logger

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import ru.tricolor.android.application.logger.user_actions_logger.base.BasicClick
import ru.tricolor.android.ui.view.MaterialCheckBoxWithError

class ViewClick(view: View) : BasicClick() {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("text")
    var text: String? = null

    @SerializedName("x")
    var x: Int

    @SerializedName("y")
    var y: Int

    @SerializedName("checked")
    var checked: Boolean? = null

    @SerializedName("position")
    var position: Int? = null

    init {
        val locationOnScreen = IntArray(2).also(view::getLocationOnScreen)
        x = locationOnScreen[0]
        y = locationOnScreen[1]

        id = getIdName(view)

        if (id == null) {
            id = getParentId(view)
        }

        if (id == null) {
            text = getText(view)
        }

        when (view.parent) {
            is RecyclerView -> {
                position = (view.parent as RecyclerView).getChildLayoutPosition(view)
            }
        }

        when (view) {
            is CompoundButton -> {
                checked = view.isChecked
            }
            is MaterialCheckBoxWithError -> {
                checked = view.isChecked
            }
        }
    }

    private fun getIdName(view: View) =
        try {
            view.resources.getResourceEntryName(view.id)
        } catch (e: Resources.NotFoundException) {
            null
        }

    private fun getParentId(view: View) =
        try {
            view.resources.getResourceEntryName((view.parent as ViewGroup).id)
        } catch (e: Resources.NotFoundException) {
            null
        }

    private fun getText(view: View): String? {
        return when (view) {
            is TextView -> {
                view.text.toString()
            }
            is ViewGroup -> {
                view.children.find { it is TextView }
                    ?.let { (it as TextView).text.toString() }
            }
            else -> null
        }
    }
}