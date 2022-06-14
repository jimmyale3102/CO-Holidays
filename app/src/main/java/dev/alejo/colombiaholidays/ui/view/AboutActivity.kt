package dev.alejo.colombiaholidays.ui.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dev.alejo.colombiaholidays.R
import dev.alejo.colombiaholidays.databinding.ActivityAboutBinding


class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val spannableString = SpannableString("Love")
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_heart)
        drawable?.let {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val span = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)
            spannableString.setSpan(
                span,
                spannableString.toString().indexOf("Love"),
                spannableString.toString().indexOf("Love") + 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            binding.description.text =spannableString
        }
    }
}