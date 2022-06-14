package dev.alejo.colombiaholidays.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.databinding.HolidayItemBinding
import java.text.SimpleDateFormat

class HolidaysViewHolder(
    private val binding: HolidayItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val dateFormat = SimpleDateFormat("EEE dd")

    fun bind(holiday: HolidayModel) {
        val dateFormatted = SimpleDateFormat("yyyy-MM-dd").parse(holiday.date)
        val day = dateFormat.format(dateFormatted!!).split(" ")[1]
        val dayName = dateFormat.format(dateFormatted).split(" ")[0]
        binding.holiday.text = holiday.localName
        binding.day.text = day
        binding.dayName.text = dayName
    }
}