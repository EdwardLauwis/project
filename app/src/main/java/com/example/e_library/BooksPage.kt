package com.example.e_library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_library.databinding.ActivityBooksPageBinding

class BooksPage : AppCompatActivity() {
    private lateinit var Binding: ActivityBooksPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_page)

        Binding = ActivityBooksPageBinding.inflate(layoutInflater)
        setContentView(Binding.root)


    }
}