package dev.alejo.colombiaholidays.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.EntryPoint
import dev.alejo.colombiaholidays.R

@EntryPoint
class MainActivity : AppCompatActivity() {

     

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}