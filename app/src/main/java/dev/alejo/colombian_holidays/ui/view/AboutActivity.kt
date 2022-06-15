package dev.alejo.colombian_holidays.ui.view

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.core.Constants.Companion.CHECHO_URL
import dev.alejo.colombian_holidays.core.Constants.Companion.JAHIR_URL
import dev.alejo.colombian_holidays.core.Constants.Companion.JIMMY_URL
import dev.alejo.colombian_holidays.core.lightStatusBar
import dev.alejo.colombian_holidays.core.setFullScreen
import dev.alejo.colombian_holidays.databinding.ActivityAboutBinding


class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun startIntent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun initAboutDescription() {
        val spannableString = SpannableString(getString(R.string.about))
        val jimmyClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startIntent(JIMMY_URL)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@AboutActivity, R.color.turquoise_blue)
            }
        }
        val chechoClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startIntent(CHECHO_URL)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@AboutActivity, R.color.turquoise_blue)
            }
        }
        val jahirClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startIntent(JAHIR_URL)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@AboutActivity, R.color.turquoise_blue)
            }
        }
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_heart)
        drawable?.let {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val span = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)
            spannableString.setSpan(
                span,
                spannableString.toString().indexOf("@"),
                spannableString.toString().indexOf("@") + 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        val jimmyStartIndex = spannableString.indexOf("Jimmy")
        val jimmyLastIndex = jimmyStartIndex + "Jimmy".length
        val chechoStartIndex = spannableString.indexOf("Checho")
        val chechoLastIndex = chechoStartIndex + "Checho".length
        val jahirStartIndex = spannableString.indexOf("Jahir")
        val jahirLastIndex = jahirStartIndex + "Jahir".length
        spannableString.setSpan(
            jimmyClickableSpan,
            jimmyStartIndex,
            jimmyLastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            chechoClickableSpan,
            chechoStartIndex,
            chechoLastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            jahirClickableSpan,
            jahirStartIndex,
            jahirLastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.description.text = spannableString
        binding.description.movementMethod = LinkMovementMethod.getInstance()
        binding.description.highlightColor = Color.TRANSPARENT
    }

    private fun initUI() {
        setFullScreen(window)
        lightStatusBar(window, true)
        initAboutDescription()
        binding.backButton.setOnClickListener { onBackPressed() }
    }
}