package dev.alejo.colombian_holidays.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import dev.alejo.colombian_holidays.core.DateUtils
import dev.alejo.colombian_holidays.data.model.HolidayModel
import dev.alejo.colombian_holidays.databinding.HolidayItemBinding
import dev.alejo.colombian_holidays.ui.view.HolidayDetailActivity
import dev.alejo.colombian_holidays.ui.view.MainActivity

@SuppressLint("SimpleDateFormat")
class HolidaysViewHolder(
    private val binding: HolidayItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, holiday: HolidayModel) {
        val dateFormatted = DateUtils.getDateFromString(holiday.date)
        dateFormatted?.let {
            val day = DateUtils.getDayFromDate(dateFormatted)
            val dayName = DateUtils.getDayNameFromDate(dateFormatted)
            binding.day.text = day
            binding.dayName.text = dayName
        }
        binding.holiday.text = holiday.localName
        binding.holidayContainer.setOnClickListener {
            MainActivity.holidaySelected = holiday
            context.startActivity(Intent(context, HolidayDetailActivity::class.java))
        }
    }
}