package ru.surfstudio.android.utilktx.util.intid


import android.content.SharedPreferences
import android.text.TextUtils
import com.annimon.stream.Stream
import ru.surfstudio.android.utilktx.util.SettingsUtil
import ru.surfstudio.android.utilktx.util.java.CollectionUtils
import java.util.*

/**
 * Хранилище для сохранения набора id, основано на SharedPref, изначально создано для хранения Enum
 */
abstract class BaseIntIdsStorage {

    private val SEPARATOR = ","
    /**
     * @return сохраненные id
     */
    val ids: MutableSet<Int>
        get() {
            val rawIds = SettingsUtil.getString(sharedPreferences(), storageKey())
            return if (TextUtils.isEmpty(rawIds))
                HashSet(0)
            else
                rawIds.split(SEPARATOR.toRegex()).map {Integer.valueOf(it) }.toMutableSet()

        }

    /**
     * @return куда писать данные
     */
    protected abstract fun sharedPreferences(): SharedPreferences

    /**
     * @return ключ под которым будут храниться данные
     */
    protected abstract fun storageKey(): String

    /**
     * Добавить id
     */
    fun addId(id: Int) {
        addIds(CollectionUtils.newSet(listOf<Int>(id)))
    }

    /**
     * Удалить id
     */
    fun removeId(id: Int) {
        removeIds(CollectionUtils.newSet(listOf<Int>(id)))
    }

    /**
     * Добавить id-шники
     */
    fun addIds(integers: Set<Int>) {
        val ids = ids
        ids.addAll(integers)
        replaceIds(ids)
    }

    /**
     * Удалить id шники
     */
    fun removeIds(integers: Set<Int>) {
        val ids = ids
        ids.removeAll(integers)
        replaceIds(ids)
    }

    /**
     * Очистить сохраненные id
     */
    fun clearSavedIds() {
        SettingsUtil.putString(sharedPreferences(), storageKey(), SettingsUtil.EMPTY_STRING_SETTING)
    }

    private fun replaceIds(ids: Set<Int>) {
        val sb = StringBuilder()
        Stream.of(ids).forEach { value -> sb
                .append(if (sb.length > 0) SEPARATOR else "")
                .append(value) }
        SettingsUtil.putString(sharedPreferences(), storageKey(), sb.toString())
    }

    companion object {


    }
}
