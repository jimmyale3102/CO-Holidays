package dev.alejo.colombiaholidays.core

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateUtils {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
    private val dateFormat = SimpleDateFormat("EEE dd")
    private val nextHolidayDateFormat = SimpleDateFormat("EEE, MMM dd")

    fun getDateFromString(dateString: String): Date? = dateFormatter.parse(dateString)
    fun getDayFromDate(date: Date): String = dateFormat.format(date).split(" ")[1]
    fun getDayNameFromDate(date: Date): String = dateFormat.format(date).split(" ")[0]
    fun getMonthNumber(dateString: String): Int = dateString.split("-")[1].toInt()
    fun getMonthFromDate(date: Date) = dateFormatter.format(date).split("-")[1].toInt()
    fun getNextHolidayFormatted(dateString: String): String =
        nextHolidayDateFormat.format(getDateFromString(dateString)!!)
    fun getDateAsInt(dateString: String): Int {
        val dateFormatted = dateFormatter.format(getDateFromString(dateString)!!)
        val year = dateFormatted.split("-")[0]
        val month = dateFormatted.split("-")[1]
        val day = dateFormatted.split("-")[2]
        return (year + month + day).toInt()
    }
}