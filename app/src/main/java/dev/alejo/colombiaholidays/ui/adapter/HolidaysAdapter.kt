package dev.alejo.colombiaholidays.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.databinding.HolidayItemBinding

class HolidaysAdapter(
    private val context: Context,
    private val holidaysList: List<HolidayModel>
): RecyclerView.Adapter<HolidaysViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidaysViewHolder {
        val binding = HolidayItemBinding.inflate(
            LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HolidaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HolidaysViewHolder, position: Int) {
        holder.bind(context, holidaysList[position])
    }

    override fun getItemCount(): Int = holidaysList.size

}