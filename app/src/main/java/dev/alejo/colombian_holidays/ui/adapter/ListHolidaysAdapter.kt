package dev.alejo.colombian_holidays.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.alejo.colombian_holidays.core.DateUtils
import dev.alejo.colombian_holidays.data.model.HolidayModel
import dev.alejo.colombian_holidays.databinding.ListHolidayItemBinding

class ListHolidaysAdapter(
    private val context: Context,
    private val holidaysList: List<HolidayModel>
): RecyclerView.Adapter<ListHolidaysViewHolder>() {

    private var currentMonth = 0
    private var showMonth = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolidaysViewHolder {
        val binding = ListHolidayItemBinding.inflate(
            LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListHolidaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolidaysViewHolder, position: Int) {
        val holiday = holidaysList[position]
        val holidayMonth = DateUtils.getMonthNumber(holiday.date)
        showMonth = holidayMonth > currentMonth
        if(showMonth)
            currentMonth = holidayMonth
        holder.bind(context, holiday, showMonth, currentMonth)
    }

    override fun getItemCount(): Int = holidaysList.size

    fun clearData() {
        currentMonth = 0
    }

}