package dev.alejo.colombiaholidays.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.model.EventDay
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import dev.alejo.colombiaholidays.R
import dev.alejo.colombiaholidays.core.Constants.Companion.CODE_200_RESPONSE
import dev.alejo.colombiaholidays.core.lightStatusBar
import dev.alejo.colombiaholidays.core.setFullScreen
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.databinding.ActivityMainBinding
import dev.alejo.colombiaholidays.ui.viewmodel.HolidayViewModel
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HolidayViewModel by viewModels()
    private val dateFormat by lazy { SimpleDateFormat("EEE, MMM dd") }
    private val holidaysList = mutableListOf<EventDay>()

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
            holidays.forEach { holiday ->
                println(holiday.date)
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, holiday.date.split("-")[0].toInt())
                calendar.set(Calendar.MONTH, holiday.date.split("-")[1].toInt() - 1)
                calendar.set(Calendar.DAY_OF_MONTH, holiday.date.split("-")[2].toInt())
                holidaysList.add(EventDay(calendar, R.drawable.dot_shape))
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

    private fun showAllHolidaysOnCalendar() {
        binding.allHolidaysContainer.calendar.setEvents(holidaysList)
    }

    private fun initUI() {
        BottomSheetBehavior.from(binding.allHolidaysBottomSheet).apply {
            peekHeight = 8
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}