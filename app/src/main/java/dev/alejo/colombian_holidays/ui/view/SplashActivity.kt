package dev.alejo.colombian_holidays.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.alejo.colombian_holidays.core.lightStatusBar
import dev.alejo.colombian_holidays.core.setFullScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(window)
        lightStatusBar(window, true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}