package dev.alejo.colombiaholidays.ui.view

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.alejo.colombiaholidays.R
import dev.alejo.colombiaholidays.core.DateUtils
import dev.alejo.colombiaholidays.core.extensions.snack
import dev.alejo.colombiaholidays.core.lightStatusBar
import dev.alejo.colombiaholidays.core.setFullScreen
import dev.alejo.colombiaholidays.databinding.ActivityHolidayDetailBinding
import dev.alejo.colombiaholidays.domain.model.HolidayNotificationItem
import dev.alejo.colombiaholidays.ui.viewmodel.HolidayDetailViewModel
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

@AndroidEntryPoint
class HolidayDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHolidayDetailBinding
    private val holidaySelected by lazy { MainActivity.holidaySelected }
    private val viewModel: HolidayDetailViewModel by viewModels()
    private val holidayNotificationId by lazy { DateUtils.getDateAsInt(holidaySelected!!.date) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHolidayDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.onCreate(this, holidayNotificationId)
        initUI()
        initObservables()
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
                val daysFormatted = if(daysDifference > 0) {
                    daysDifference + 1
                } else {
                    binding.setNotification.visibility = View.GONE
                    daysDifference.absoluteValue
                }
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

    private fun initObservables() {
        viewModel.holidayNotificationResponse.observe(this) { holidayNotification ->
            if(holidayNotification == null)
                turnNotificationButtonOff()
            else
                turnNotificationButtonOn()
            Log.e("Notification->", holidayNotification?.toString() ?: "It's Null")
        }
    }

    private fun removeNotification() {
        viewModel.removeHolidayNotification(holidayNotificationId)
        viewModel.removeNotification(applicationContext, holidayNotificationId)
        turnNotificationButtonOff()
        snack(binding.root, R.string.notification_removed)
    }

    private fun insertNotification() {
        val notificationTime = DateUtils.getTimeFromString(holidaySelected!!.date)
        viewModel.scheduleNotification(
            applicationContext,
            holidayNotificationId,
            holidaySelected?.localName ?: "",
            notificationTime
        )
        viewModel.insertHolidayNotification(HolidayNotificationItem(holidayNotificationId))
        turnNotificationButtonOn()
        snack(binding.root, R.string.notification_scheduled)
    }

    private fun turnNotificationButtonOff() {
        binding.setNotification.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(applicationContext, R.color.light_gray)
        )
        binding.setNotification.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.ic_notification_off)
        )
        binding.setNotification.imageTintList =
            ColorStateList.valueOf(getColor(R.color.turquoise_blue))
    }

    private fun turnNotificationButtonOn() {
        binding.setNotification.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(applicationContext, R.color.turquoise_blue)
        )
        binding.setNotification.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.ic_notification_on)
        )
        binding.setNotification.imageTintList =
            ColorStateList.valueOf(getColor(R.color.light_gray))
    }

    private fun initUI() {
        setFullScreen(window)
        lightStatusBar(window, true)
        binding.setNotification.setOnClickListener {
            if(viewModel.existHolidayNotification.value!!)
                removeNotification()
            else
                insertNotification()
        }
        binding.backButton.setOnClickListener { onBackPressed() }
    }
}