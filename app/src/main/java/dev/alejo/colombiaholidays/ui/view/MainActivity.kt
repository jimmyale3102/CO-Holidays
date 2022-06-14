package dev.alejo.colombiaholidays.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.model.EventDay
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import dev.alejo.colombiaholidays.R
import dev.alejo.colombiaholidays.core.Constants.Companion.CODE_200_RESPONSE
import dev.alejo.colombiaholidays.core.lightStatusBar
import dev.alejo.colombiaholidays.core.setFullScreen
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.databinding.ActivityMainBinding
import dev.alejo.colombiaholidays.ui.adapter.HolidaysAdapter
import dev.alejo.colombiaholidays.ui.adapter.ListHolidaysAdapter
import dev.alejo.colombiaholidays.ui.viewmodel.HolidayViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HolidayViewModel by viewModels()
    private val dateFormat by lazy { SimpleDateFormat("EEE, MMM dd") }
    private val holidaysEventList = mutableListOf<EventDay>()
    private val holidaysList = mutableListOf<HolidayModel>()
    private val calendarAdapter by lazy { HolidaysAdapter(holidaysList) }
    private val holidaysListAdapter by lazy { ListHolidaysAdapter(this, holidaysList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(window)
        lightStatusBar(window, false)
        viewModel.onCreate()
        initObservables()
        initUI()
    }

    private fun initObservables() {
        viewModel.holidayByYearResponse.observe(this) { holidays ->
            holidaysList.clear()
            holidaysList.addAll(holidays)
            holidays.forEach { holiday ->
                println(holiday.date)
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, holiday.date.split("-")[0].toInt())
                calendar.set(Calendar.MONTH, holiday.date.split("-")[1].toInt() - 1)
                calendar.set(Calendar.DAY_OF_MONTH, holiday.date.split("-")[2].toInt())
                holidaysEventList.add(EventDay(calendar, R.drawable.dot_shape))
            }
            runOnUiThread { showAllHolidaysOnCalendar() }
        }
        viewModel.nextPublicHolidayResponse.observe(this) { holiday ->
            runOnUiThread { showNextHoliday(holiday) }
        }
        viewModel.todayHolidayResponse.observe(this) { todayHoliday ->
            runOnUiThread { showTodayHoliday(todayHoliday) }
        }
    }

    private fun showTodayHoliday(todayHoliday: String) {
        binding.holiday.text = todayHoliday
    }

    @SuppressLint("SetTextI18n")
    private fun showNextHoliday(nextHoliday: HolidayModel) {
        val holidayDate = SimpleDateFormat("yyyy-MM-dd").parse(nextHoliday.date)
        val dateFormatted = holidayDate?.let { date ->
            dateFormat.format(date)
        }
        if(binding.holiday.text.isEmpty() || binding.holiday.text != CODE_200_RESPONSE) {
            binding.holidayDescription.text = getString(R.string.upcoming_holiday) +
                " $dateFormatted ${getString(R.string.next_holiday_for)} ${nextHoliday.localName}"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAllHolidaysOnCalendar() {
        calendarAdapter.notifyDataSetChanged()
        holidaysListAdapter.notifyDataSetChanged()
        binding.allHolidaysContainer.calendar.setEvents(holidaysEventList)
    }

    private fun initUI() {
        val calendarLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val listLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.allHolidaysContainer.calendarRecycler.layoutManager = calendarLayoutManager
        binding.allHolidaysContainer.calendarRecycler.adapter = calendarAdapter
        binding.allHolidaysContainer.listRecycler.layoutManager = listLayoutManager
        binding.allHolidaysContainer.listRecycler.adapter = holidaysListAdapter
        BottomSheetBehavior.from(binding.allHolidaysBottomSheet).apply {
            peekHeight = 8
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}