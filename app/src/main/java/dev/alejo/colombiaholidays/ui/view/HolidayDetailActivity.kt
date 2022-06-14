package dev.alejo.colombiaholidays.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.alejo.colombiaholidays.databinding.ActivityHolidayDetailBinding

class HolidayDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHolidayDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHolidayDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.backButton.setOnClickListener { onBackPressed() }
    }
}