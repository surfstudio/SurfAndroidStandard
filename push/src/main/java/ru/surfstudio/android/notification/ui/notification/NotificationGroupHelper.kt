package ru.surfstudio.android.notification.ui.notification

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 *
 */
internal object NotificationGroupHelper {

    /**
     * This method will save all the notifications on shared pereference
     * and will return the String array list which will be processed.
     * @param context context of current service
     * @param title   title of notification. Not used in this demo mode.
     * @param desc    String to save in SP.
     * @return        String value of ArrayList.class object.
     */
    fun processStringDatas(context: Context, groupId: Int, desc: String): List<String> {

        val sp = getSharedPref(context)

        val notificationObject: MutableList<String>

        val notifContent = sp.getString(getGroupKey(groupId), null)

        notificationObject = if (notifContent != null) {
            getArrayFromJson(notifContent)
        } else {
            ArrayList()
        }
        notificationObject.add(desc)

        sp.edit().putString(getGroupKey(groupId), listToJsonString(notificationObject)).apply()

        return notificationObject
    }

    fun clearStringDatas(context: Context, groupId: Int) {
        val sp = getSharedPref(context)
        sp.edit().putString(getGroupKey(groupId), EMPTY_STRING).apply()
    }

    private fun getArrayFromJson(jsonString: String): MutableList<String> {
        val res = ArrayList<String>()
        val jsonArray = JSONArray(jsonString)
        for (i in (0 until jsonArray.length())) {
            res.add(jsonArray[i].toString())
        }
        return res
    }

    private fun listToJsonString(list: List<String>): String {
        return list.toString()
    }

    private fun getSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences("notifications", Context.MODE_PRIVATE)
    }

    private fun getGroupKey(groupId: Int) = "notif_content_$groupId"
}