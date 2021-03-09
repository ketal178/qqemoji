package com.ketal.qemoji.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.ketal.qemoji.R

class SettingsActivity : AppCompatTransferActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_Ftb)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
