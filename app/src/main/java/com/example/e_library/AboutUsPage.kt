package com.example.e_library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_library.databinding.ActivityAboutUsPageBinding

class AboutUsPage : AppCompatActivity() {
    private lateinit var binding: ActivityAboutUsPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us_page)

        binding = ActivityAboutUsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}