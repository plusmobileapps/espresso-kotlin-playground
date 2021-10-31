package com.plusmobileapps.kotlinopenespresso.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.plusmobileapps.kotlinopenespresso.databinding.SettingsBinding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}