package ru.surfstudio.android.util

import android.annotation.SuppressLint
import ru.surfstudio.android.ktx.text.EMPTY_STRING
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object TimeUtils {

    //если необходима эта дата как Date необходимо использовать floorDate.time
    val floorDate = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 31)
        //0..11
        set(Calendar.MONTH, 11)
        set(Calendar.YEAR, 1917)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDate(date: String?,
                    inputFormat: String,
                    outputFormat: String): String {
        if (date.isNullOrEmpty()) {
            return EMPTY_STRING
        }
        val inputSdf = SimpleDateFormat(inputFormat)
        val parsedDate: Date = try {
            inputSdf.parse(date)
        } catch (e: ParseException) {
            return EMPTY_STRING
        }
        return convertDate(parsedDate, outputFormat)
    }

    fun convertDate(date: Date?, outputFormat: String): String = SimpleDateFormat(outputFormat).format(date)

    fun getDaysBeforeTheDate(date: Date?): Int {

        val calendar = Calendar.getInstance()

        //получаем количество дней в текущем году
        // и порядковый номер сегодняшнего дня в текущем году
        calendar.time = Date()
        val daysCount = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
        val currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        //получаем порядковый номер дня до которого необходимо посчитать количество оставшихся дней
        calendar.time = date
        val futureDayOfTheYear = calendar.get(Calendar.DAY_OF_YEAR)

        return if (currentDayOfYear <= futureDayOfTheYear)
            futureDayOfTheYear - currentDayOfYear
        else
            daysCount - currentDayOfYear + futureDayOfTheYear
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDate(date: String?,
                  inputFormat: String): Date {
        if (date.isNullOrEmpty()) {
            return Date()
        }
        val inputSdf = SimpleDateFormat(inputFormat)
        return inputSdf.parse(date)
    }
}