package dev.alejo.colombiaholidays.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import dev.alejo.colombiaholidays.core.DateUtils
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.databinding.HolidayItemBinding

@SuppressLint("SimpleDateFormat")
class HolidaysViewHolder(
    private val binding: HolidayItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(holiday: HolidayModel) {
        val dateFormatted = DateUtils.getDateFromString(holiday.date)
        dateFormatted?.let {
            val day = DateUtils.getDayFromDate(dateFormatted)
            val dayName = DateUtils.getDayNameFromDate(dateFormatted)
            binding.day.text = day
            binding.dayName.text = dayName
        }
        binding.holiday.text = holiday.localName
    }
}