package ru.surfstudio.standard.ui.util


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UiDateUtil {

    private val ROLL_DATE_PATTERN = "HH:mm"
    private val sdf = SimpleDateFormat(ROLL_DATE_PATTERN, Locale.getDefault())

    /**
     * @return дата в формате "15:00"
     */
    fun formatDateForDelivery(date: Date): String {
        return sdf.format(date)
    }

    fun formatDateForDeliveryEmptyIfNull(date: Date?): String {
        return if (date != null) formatDateForDelivery(date) else ""
    }
}
