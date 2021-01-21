/*
  Copyright (c) 2018-present, SurfStudio LLC, Akhbor Akhrorov.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.notification.ui.notification

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Helper class for Grouping notifications for API version <= M
 */
internal object NotificationGroupHelper {

    /**
     * This method will save all the notifications on shared preference
     * and will return the String array list which will be processed.
     * @param context context of current service
     * @param desc    String to save in SP.
     * @return [List] of [String] objects (saved notifications for given groupId)
     */
    fun getNotificationsForGroup(context: Context, groupId: Int, desc: String): List<String> {

        val sp = getSharedPref(context)

        val notificationObject: MutableList<String>

        val notifContent: String = sp.getString(getGroupKey(groupId), EMPTY_STRING) ?: EMPTY_STRING
        notificationObject = getArrayFromJson(notifContent)
        notificationObject.add(desc)

        sp.edit().putString(getGroupKey(groupId), listToJsonString(notificationObject)).apply()

        return notificationObject
    }

    /**
     * This method will clear cached notifications for given group
     */
    fun clearSavedNotificationsForGroup(context: Context, groupId: Int) {
        val sp = getSharedPref(context)
        sp.edit().putString(getGroupKey(groupId), EMPTY_STRING).apply()
    }

    private fun getArrayFromJson(jsonString: String): MutableList<String> {
        val res = ArrayList<String>()
        if(jsonString == EMPTY_STRING) return res

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