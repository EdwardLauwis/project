package com.example.e_library

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_library.databinding.ActivityEditBookPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditBookPage : AppCompatActivity(), OnClickListener{
    private lateinit var binding: ActivityEditBookPageBinding
    lateinit var bookTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book_page)

        binding = ActivityEditBookPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookTitle = intent.getStringExtra("bookTitle").toString()
        Toast.makeText(this, bookTitle, Toast.LENGTH_SHORT).show()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        supportActionBar?.title = ""

        binding.ButtonSave.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                val intent = Intent(this@EditBookPage, BooksPage::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == binding.ButtonSave){
            val title = binding.EditTextTitle.text.toString()
            val year = binding.EditTextYear.text.toString()
            val genre = binding.EditTextGenre.text.toString()
            val desc = binding.EditTextDescription.text.toString()

            val firebaseDatabase = FirebaseDatabase.getInstance()
            val reference : DatabaseReference = firebaseDatabase.getReference("Books")

            fun CheckBook(callback: (Boolean) -> Unit){
                reference.addListenerForSingleValueEvent(object : ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var isUpdated = false
                        for(childSnapshot in snapshot.children){
                            if (childSnapshot.child("title").getValue(String::class.java).equals(bookTitle)){
                                if (title.isNotEmpty())
                                    childSnapshot.ref.child("title").setValue(title)

                                if (year.isNotEmpty())
                                    childSnapshot.ref.child("year").setValue(year)

                                if (genre.isNotEmpty())
                                    childSnapshot.ref.child("genre").setValue(genre)

                                if (desc.isNotEmpty())
                                    childSnapshot.ref.child("description").setValue(desc)

                                isUpdated = true
                                break
                            }
                        }

                        callback(isUpdated)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback(false)
                    }

                })
            }

            CheckBook (){ isUpdated ->
                if (isUpdated){
                    Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@EditBookPage, BooksPage::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}