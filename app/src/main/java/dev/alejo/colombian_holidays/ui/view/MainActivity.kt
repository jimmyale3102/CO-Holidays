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
    private val calendarAdapter by lazy { HolidaysAdapter(this, calendarHolidaysList) }
    private val holidaysListAdapter by lazy { ListHolidaysAdapter(this, holidaysList) }
    private var currentCalendarYear = DateUtils.getYearFromDate(Date(System.currentTimeMillis()))
    private var currentCalendarMonth = 0
    private var yearValidated = false
    private val ALL_HOLIDAYS = 1
    private val CALENDAR_HOLIDAYS = 0
    private var displaying = CALENDAR_HOLIDAYS
    private val generalCalendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.MONTH, 0)
        set(Calendar.YEAR, currentCalendarYear)
    }
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
        viewModel.onCreate(this)
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
        viewModel.isTodayHolidayLoading.observe(this) { isLoading ->
            if(isLoading){
                binding.holidayDataContent.visibility = View.GONE
                binding.loadingContent.visibility = View.VISIBLE
            } else {
                binding.holidayDataContent.visibility = View.VISIBLE
                binding.loadingContent.visibility = View.GONE
            }
        }
        viewModel.isGetHolidayByYearLoading.observe(this) { isLoading ->
            binding.allHolidaysContainer.loadingContent.visibility =
                if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showTodayHoliday(todayHoliday: String) {
        binding.holiday.text = todayHoliday
    }

    @SuppressLint("SetTextI18n")
    private fun showNextHoliday(nextHoliday: HolidayModel) {
        binding.holidayDescription.text = getString(R.string.upcoming_holiday) +
            " ${DateUtils.getNextHolidayFormatted(nextHoliday.date)} " +
            "${getString(R.string.next_holiday_for)} " +
            if(Locale.getDefault().language == "es") nextHoliday.localName else nextHoliday.name
    }

    private fun showAllHolidaysOnCalendar() {
        updateMonthHolidays()
        holidaysListAdapter.clearData()
        holidaysListAdapter.notifyDataSetChanged()
        binding.allHolidaysContainer.calendar.setEvents(holidaysEventList)
        binding.allHolidaysContainer.currentYear.text = currentCalendarYear.toString()
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
        yearValidated = false
    }

    private fun validateCurrentYear() {
        if(!yearValidated) {
            yearValidated = true
            val calendarYear = DateUtils.getYearFromDate(
                binding.allHolidaysContainer.calendar.currentPageDate.time
            )
            if(calendarYear != currentCalendarYear) {
                currentCalendarYear = calendarYear
                calendarHolidaysList.clear()
                viewModel.getHolidayByYear(currentCalendarYear.toString())
            } else {
                updateMonthHolidays()
            }
        }
    }

    private fun updateCalendarYear(newYear: Int) {
        generalCalendar.set(Calendar.YEAR, newYear)
        generalCalendar.set(Calendar.MONTH, currentCalendarMonth - 1)
        currentCalendarMonth = 0
        binding.allHolidaysContainer.calendar.setDate(generalCalendar.time)
    }

    private fun checkAllHolidaysContent() {
        if(binding.allHolidaysContainer.calendarContent.visibility == View.VISIBLE) {
            binding.allHolidaysContainer.calendarContent.visibility = View.GONE
            binding.allHolidaysContainer.listRecycler.visibility = View.VISIBLE
            binding.allHolidaysContainer.yearContent.visibility = View.VISIBLE
            binding.contentFormatButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_calendar)
            )
            displaying = ALL_HOLIDAYS
        } else {
            binding.allHolidaysContainer.calendarContent.visibility = View.VISIBLE
            binding.allHolidaysContainer.listRecycler.visibility = View.GONE
            binding.allHolidaysContainer.yearContent.visibility = View.GONE
            binding.contentFormatButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_list)
            )
            displaying = CALENDAR_HOLIDAYS
        }
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
        binding.allHolidaysContainer.currentYear.text = currentCalendarYear.toString()
        binding.allHolidaysContainer.calendar.setOnForwardPageChangeListener {
            validateCurrentYear()
        }
        binding.allHolidaysContainer.calendar.setOnPreviousPageChangeListener {
            validateCurrentYear()
        }
        binding.allHolidaysContainer.previousYearButton.setOnClickListener {
            updateCalendarYear(currentCalendarYear-1)
            validateCurrentYear()
        }
        binding.allHolidaysContainer.nextYearButton.setOnClickListener {
            updateCalendarYear(currentCalendarYear+1)
            validateCurrentYear()
        }
        binding.contentFormatButton.setOnClickListener {
            checkAllHolidaysContent()
        }
        BottomSheetBehavior.from(binding.allHolidaysBottomSheet).apply {
            peekHeight = 150
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