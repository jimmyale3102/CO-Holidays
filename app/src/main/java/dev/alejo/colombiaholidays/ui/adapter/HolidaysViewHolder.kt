package dev.alejo.colombiaholidays.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.databinding.HolidayItemBinding

class HolidaysViewHolder(private val binding: HolidayItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HolidayModel) {
        binding.holiday.text = item.localName
    }
}