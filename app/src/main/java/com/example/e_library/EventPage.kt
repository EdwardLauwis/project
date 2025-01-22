package com.example.e_library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_library.databinding.ActivityEventPageBinding

class EventPage : AppCompatActivity() {

    private lateinit var binding: ActivityEventPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_page)

        binding = ActivityEventPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}