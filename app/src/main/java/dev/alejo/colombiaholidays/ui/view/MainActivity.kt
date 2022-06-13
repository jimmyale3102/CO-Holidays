package dev.alejo.colombiaholidays.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import dev.alejo.colombiaholidays.core.lightStatusBar
import dev.alejo.colombiaholidays.core.setFullScreen
import dev.alejo.colombiaholidays.databinding.ActivityMainBinding
import dev.alejo.colombiaholidays.ui.viewmodel.HolidayViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HolidayViewModel by viewModels()

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
            holidays.forEach { it -> println(it.localName) }
        }
        viewModel.nextPublicHolidayResponse.observe(this) { holiday ->
            println("*********************************************************")
            println(holiday.toString())
        }
        viewModel.todayHolidayResponse.observe(this) { todayHoliday ->
            runOnUiThread { showTodayHoliday(todayHoliday) }
        }
    }

    private fun showTodayHoliday(todayHoliday: String) {
        binding.holiday.text = todayHoliday
    }

    private fun initUI() {
        BottomSheetBehavior.from(binding.allHolidaysBottomSheet).apply {
            peekHeight = 64
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}