package com.example.e_library

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.Toast
import com.example.e_library.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var Binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        val linearLayoutEvents: LinearLayout = Binding.LinearLayoutEvents
        val linearLayoutBooks: LinearLayout = Binding.LinearLayoutBooks

        val backgroundEvent = linearLayoutEvents.background
        val backgroundBooks = linearLayoutBooks.background

        if (backgroundEvent is AnimationDrawable) {
            val animationDrawableBooks = backgroundBooks as AnimationDrawable
            backgroundEvent.setEnterFadeDuration(2500)
            backgroundEvent.setExitFadeDuration(4000)
            backgroundEvent.start()

            animationDrawableBooks.setEnterFadeDuration(2500)
            animationDrawableBooks.setExitFadeDuration(4000)
            animationDrawableBooks.start()
        } else {
            Toast.makeText(this, "Background is not an AnimationDrawable", Toast.LENGTH_SHORT).show()
        }

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("userSession")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java).toString()
                val password = snapshot.child("password").getValue(String::class.java).toString()
                val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java).toString()
                val booksRead = snapshot.child("booksRead").getValue(Int::class.java)?.toInt()

                Toast.makeText(this@MainActivity, "$booksRead", Toast.LENGTH_SHORT).show()

                val userTemp = if (booksRead != null) {
                    User(username, password, phoneNumber, booksRead)
                } else {
                    null
                }

                userSession.session = userTemp ?: User()

                if (userSession.session.username.isEmpty()) {
                    val intent = Intent(this@MainActivity, RegisterPage::class.java)
                    startActivity(intent)
                    finish()
                }
                Binding.TextViewUsername.text = username
                Binding.TextViewBooksRead.text = booksRead.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })

        Binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.NavigationHome -> {
                    true
                }
                R.id.NavigationSearch -> {
                    true
                }
                R.id.NavigationProfile -> {
                    val intent = Intent(this, ProfilePage::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        Binding.LinearLayoutEvents.setOnClickListener(this)
        Binding.LinearLayoutBooks.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 == Binding.LinearLayoutEvents){

        } else if (p0 == Binding.LinearLayoutBooks){
            val intent = Intent(this@MainActivity, BooksPage::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.ButtonLogOut -> {
                val firebaseDatabase = FirebaseDatabase.getInstance()
                val databaseReference = firebaseDatabase.getReference("userSession")

                val user = User("", "", "", 0)

                databaseReference.setValue(user)

                val intent = Intent(this@MainActivity, RegisterPage::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
