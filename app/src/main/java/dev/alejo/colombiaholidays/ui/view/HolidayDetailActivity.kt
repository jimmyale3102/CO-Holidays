package dev.alejo.colombiaholidays.ui.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.alejo.colombiaholidays.R
import dev.alejo.colombiaholidays.core.Constants.Companion.CHANNEL_ID
import dev.alejo.colombiaholidays.core.Constants.Companion.MESSAGE_EXTRA
import dev.alejo.colombiaholidays.core.Constants.Companion.NOTIFICATION_ID
import dev.alejo.colombiaholidays.core.DateUtils
import dev.alejo.colombiaholidays.core.Notification
import dev.alejo.colombiaholidays.core.lightStatusBar
import dev.alejo.colombiaholidays.core.setFullScreen
import dev.alejo.colombiaholidays.databinding.ActivityHolidayDetailBinding
import dev.alejo.colombiaholidays.ui.viewmodel.HolidayDetailViewModel
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class HolidayDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHolidayDetailBinding
    private val holidaySelected by lazy { MainActivity.holidaySelected }
    private val viewModel: HolidayDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHolidayDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.onCreate(this)
        initUI()
        showData()
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        holidaySelected?.let { holiday ->
            val date = DateUtils.getDateFromString(holiday.date)
            binding.holiday.text = holiday.name
            binding.date.text = DateUtils.getNextHolidayFormatted(holiday.date)
            date?.let { holidayDate ->
                val daysDifference = TimeUnit.MILLISECONDS.toDays(
                    holidayDate.time - System.currentTimeMillis()
                )
                val daysFormatted = if(daysDifference > 0)
                    daysDifference + 1
                else
                    daysDifference.absoluteValue
                binding.daysLeft.text = "$daysFormatted " +
                    if(daysDifference < 0) getString(R.string.ago) else getString(R.string.days_left)
            }
            if(holiday.fixed)
                binding.isSameDate.visibility = View.VISIBLE
            if(holiday.global)
                binding.isEveryCounty.visibility = View.VISIBLE
            holiday.launchYear?.let { launchYear ->
                binding.launchYear.text = "${getString(R.string.launch_year)} $launchYear"
            }
            binding.type.text = "${getString(R.string.type)} ${getString(R.string.public_type)}"
        }
    }

    private fun initUI() {
        setFullScreen(window)
        lightStatusBar(window, true)
        binding.setNotification.setOnClickListener {
            viewModel.scheduleNotification(
                applicationContext,
                DateUtils.getDateAsInt(holidaySelected!!.date),
                holidaySelected?.name ?: ""
            )
        }
        binding.backButton.setOnClickListener { onBackPressed() }
    }
}