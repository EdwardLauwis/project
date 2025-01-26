package com.example.e_library

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_library.databinding.ActivityAddBookPageBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddBookPage : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityAddBookPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book_page)

        binding = ActivityAddBookPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        supportActionBar?.title = ""

        binding.ButtonAdd.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        if (p0 == binding.ButtonAdd){
            val databaseReference: DatabaseReference = firebaseDatabase.getReference("Books")

            val title: String = binding.EditTextTitle.text.toString()
            val year: Int = binding.EditTextYear.text.toString().toIntOrNull()?:0
            val genre: String = binding.EditTextGenre.text.toString()
            val description: String = binding.EditTextDescription.text.toString()

            if (title.isEmpty() || genre.isEmpty() || description.isEmpty() || year == 0) {
                binding.TextViewError.setTextColor(resources.getColor(R.color.red, theme))
                binding.TextViewError.text = "Please fill in all fields correctly."
                return
            }
            val book = mapOf(
                "title" to title,
                "year" to year,
                "genre" to genre,
                "description" to description
            )

            databaseReference.push().setValue(book).addOnSuccessListener {
                binding.TextViewError.setTextColor(resources.getColor(R.color.green, theme))
                binding.TextViewError.text = "Input Success"
                binding.EditTextTitle.setText("")
                binding.EditTextYear.setText("")
                binding.EditTextGenre.setText("")
                binding.EditTextDescription.setText("")
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}