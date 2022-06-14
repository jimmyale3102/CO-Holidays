package dev.alejo.colombiaholidays.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.alejo.colombiaholidays.R
import dev.alejo.colombiaholidays.core.DateUtils
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.databinding.ListHolidayItemBinding

@SuppressLint("SimpleDateFormat")
class ListHolidaysViewHolder(
    private val binding: ListHolidayItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, holiday: HolidayModel, showMonth: Boolean, currentMonth: Int) {
        val dateFormatted = DateUtils.getDateFromString(holiday.date)
        dateFormatted?.let {
            val day = DateUtils.getDayFromDate(dateFormatted)
            val dayName = DateUtils.getDayNameFromDate(dateFormatted)
            binding.holidayItem.day.text = day
            binding.holidayItem.dayName.text = dayName
        }
        if(showMonth) {
            binding.monthName.text = when(currentMonth) {
                1 -> context.getString(R.string.january)
                2 -> context.getString(R.string.february)
                3 -> context.getString(R.string.march)
                4 -> context.getString(R.string.april)
                5 -> context.getString(R.string.may)
                6 -> context.getString(R.string.june)
                7 -> context.getString(R.string.july)
                8 -> context.getString(R.string.august)
                9 -> context.getString(R.string.september)
                10 -> context.getString(R.string.october)
                11 -> context.getString(R.string.november)
                12 -> context.getString(R.string.december)
                else -> ""
            }
            binding.monthName.visibility = View.VISIBLE
        }
        binding.holidayItem.holiday.text = holiday.localName
    }
}