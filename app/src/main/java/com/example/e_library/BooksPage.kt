package com.example.e_library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import com.example.e_library.databinding.ActivityBooksPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BooksPage : AppCompatActivity(), OnClickListener{
    private lateinit var binding: ActivityBooksPageBinding
    private lateinit var booksList: ArrayList<book>
    private lateinit var adapter: BooksAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_page)

        binding = ActivityBooksPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        booksList = ArrayList()
        adapter = BooksAdapter(this,booksList)
        binding.ListViewBooks.adapter = adapter

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Books")


        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                booksList.clear()
                for (postSnapshot in snapshot.children){
                    val books = postSnapshot.getValue(book::class.java)
                    booksList.add(books!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        supportActionBar?.title = ""

        binding.buttonAdd.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 == binding.buttonAdd){
            val intent = Intent(this@BooksPage, AddBookPage::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this@BooksPage, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}