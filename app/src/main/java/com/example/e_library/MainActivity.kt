package com.example.e_library

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.e_library.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(this, binding.DrawerLayoutParent, R.string.open_drawer, R.string.close_drawer)
        binding.DrawerLayoutParent.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val linearLayoutEvents: LinearLayout = binding.LinearLayoutEvents
        val linearLayoutBooks: LinearLayout = binding.LinearLayoutBooks

        val backgroundEvent = linearLayoutEvents.background
        val backgroundBooks = linearLayoutBooks.background

        if (backgroundEvent is AnimationDrawable && backgroundBooks is AnimationDrawable) {
            backgroundEvent.setEnterFadeDuration(2500)
            backgroundEvent.setExitFadeDuration(4000)
            backgroundEvent.start()

            backgroundBooks.setEnterFadeDuration(2500)
            backgroundBooks.setExitFadeDuration(4000)
            backgroundBooks.start()
        } else {
            Toast.makeText(this, "Background is not an AnimationDrawable", Toast.LENGTH_SHORT).show()
        }

        val databaseReference = FirebaseDatabase.getInstance().getReference("userSession")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java).orEmpty()
                val password = snapshot.child("password").getValue(String::class.java).orEmpty()
                val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java).orEmpty()
                val booksRead = snapshot.child("booksRead").getValue(Int::class.java) ?: 0

                userSession.session = User(username, password, phoneNumber, booksRead)

                if (userSession.session.username.isEmpty()) {
                    startActivity(Intent(this@MainActivity, RegisterPage::class.java))
                    finish()
                }

                binding.TextViewUsername.text = username
                binding.TextViewBooksRead.text = booksRead.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.NavigationHome -> true
                R.id.NavigationSearch -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.NavigationProfile -> {
                    startActivity(Intent(this, ProfilePage::class.java))
                    true
                }
                else -> false
            }
        }

        binding.LinearLayoutEvents.setOnClickListener(this)
        binding.LinearLayoutBooks.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.LinearLayoutEvents -> Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
            binding.LinearLayoutBooks -> startActivity(Intent(this, BooksPage::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.aboutus, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.DrawerLayoutParent.isDrawerOpen(GravityCompat.START)) {
                    binding.DrawerLayoutParent.closeDrawer(GravityCompat.START)
                } else {
                    binding.DrawerLayoutParent.openDrawer(GravityCompat.START)
                }
                true
            }
            R.id.ButtonAboutUs -> {
                startActivity(Intent(this, AboutUsPage::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
