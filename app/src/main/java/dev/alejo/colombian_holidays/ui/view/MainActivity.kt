package dev.alejo.colombian_holidays.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.model.EventDay
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_200_RESPONSE
import dev.alejo.colombian_holidays.core.Constants.Companion.NOTIFICATION_ID_EXTRA
import dev.alejo.colombian_holidays.core.DateUtils
import dev.alejo.colombian_holidays.core.lightStatusBar
import dev.alejo.colombian_holidays.core.setFullScreen
import dev.alejo.colombian_holidays.data.model.HolidayModel
import dev.alejo.colombian_holidays.databinding.ActivityMainBinding
import dev.alejo.colombian_holidays.ui.adapter.HolidaysAdapter
import dev.alejo.colombian_holidays.ui.adapter.ListHolidaysAdapter
import dev.alejo.colombian_holidays.ui.viewmodel.HolidayDetailViewModel
import dev.alejo.colombian_holidays.ui.viewmodel.HolidayViewModel
import java.util.*

@SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HolidayViewModel by viewModels()
    private val holidayDetailsViewModel: HolidayDetailViewModel by viewModels()
    private val holidaysEventList = mutableListOf<EventDay>()
    private val holidaysList = mutableListOf<HolidayModel>()
    private val calendarHolidaysList = mutableListOf<HolidayModel>()
    private var currentCalendarMonth = 0
    private var currentCalendarYear = 0
    private val calendarAdapter by lazy { HolidaysAdapter(this, calendarHolidaysList) }
    private val holidaysListAdapter by lazy { ListHolidaysAdapter(this, holidaysList) }
    private val backgroundDrawables = arrayListOf(
        R.drawable.background_1,
        R.drawable.background_2,
        R.drawable.background_3,
        R.drawable.background_4,
        R.drawable.background_5,
        R.drawable.background_6,
        R.drawable.background_7,
        R.drawable.background_8,
        R.drawable.background_9,
        R.drawable.background_10,
        R.drawable.background_11,
        R.drawable.background_12,
        R.drawable.background_13,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(window)
        lightStatusBar(window, false)
        viewModel.onCreate()
        if(intent.hasExtra(NOTIFICATION_ID_EXTRA))
            updateDatabase()
        initObservables()
        initUI()
    }

    private fun updateDatabase() {
        holidayDetailsViewModel.removeHolidayNotification(
            intent.getIntExtra(NOTIFICATION_ID_EXTRA, 0)
        )
    }

    private fun initObservables() {
        viewModel.holidayByYearResponse.observe(this) { holidays ->
            holidaysList.clear()
            holidaysList.addAll(holidays)
            holidaysEventList.clear()
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
        if(binding.holiday.text.isEmpty() || binding.holiday.text != CODE_200_RESPONSE) {
            binding.holidayDescription.text = getString(R.string.upcoming_holiday) +
                " ${DateUtils.getNextHolidayFormatted(nextHoliday.date)} " +
                "${getString(R.string.next_holiday_for)} ${nextHoliday.localName}"
        }
    }

    private fun showAllHolidaysOnCalendar() {
        updateMonthHolidays()
        holidaysListAdapter.notifyDataSetChanged()
        binding.allHolidaysContainer.calendar.setEvents(holidaysEventList)
    }

    private fun updateMonthHolidays() {
        val calendarMonth = DateUtils.getMonthFromDate(
            binding.allHolidaysContainer.calendar.currentPageDate.time
        )
        if(calendarMonth != currentCalendarMonth) {
            currentCalendarMonth = calendarMonth
            calendarHolidaysList.clear()
            calendarHolidaysList.addAll(
                holidaysList.filter { DateUtils.getMonthNumber(it.date) == currentCalendarMonth }
            )
            calendarAdapter.notifyDataSetChanged()
        }
    }

    private fun validateCurrentYear() {
        val calendarYear = DateUtils.getYearFromDate(
            binding.allHolidaysContainer.calendar.currentPageDate.time
        )
        if(calendarYear != currentCalendarYear) {
            currentCalendarYear = calendarYear
            calendarHolidaysList.clear()
            viewModel.getHolidayByYear(currentCalendarYear.toString())
        }
        updateMonthHolidays()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initUI() {
        binding.backgroundDrawable.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                backgroundDrawables[(0 until backgroundDrawables.size).random()]
            )
        )
        val calendarLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val listLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.allHolidaysContainer.calendarRecycler.layoutManager = calendarLayoutManager
        binding.allHolidaysContainer.calendarRecycler.adapter = calendarAdapter
        binding.allHolidaysContainer.listRecycler.layoutManager = listLayoutManager
        binding.allHolidaysContainer.listRecycler.adapter = holidaysListAdapter
        binding.allHolidaysContainer.calendar.setOnForwardPageChangeListener {
            validateCurrentYear()
        }
        binding.allHolidaysContainer.calendar.setOnPreviousPageChangeListener {
            validateCurrentYear()
        }
        binding.contentFormatButton.setOnClickListener {
            if(binding.allHolidaysContainer.calendarContent.visibility == View.VISIBLE) {
                binding.allHolidaysContainer.calendarContent.visibility = View.GONE
                binding.allHolidaysContainer.listRecycler.visibility = View.VISIBLE
                binding.contentFormatButton.setImageDrawable(getDrawable(R.drawable.ic_calendar))
            } else {
                binding.allHolidaysContainer.calendarContent.visibility = View.VISIBLE
                binding.allHolidaysContainer.listRecycler.visibility = View.GONE
                binding.contentFormatButton.setImageDrawable(getDrawable(R.drawable.ic_list))
            }
        }
        BottomSheetBehavior.from(binding.allHolidaysBottomSheet).apply {
            peekHeight = 8
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    companion object {
        var holidaySelected: HolidayModel? = null
    }
}