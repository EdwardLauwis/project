package com.example.e_library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.e_library.databinding.ActivityProfilePageBinding
import com.google.firebase.database.FirebaseDatabase

class ProfilePage : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityProfilePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.ProfileUsername.text = userSession.session.username
        binding.ProfilePassword.text = passMaker()
        binding.ProfilePhoneNumber.text = userSession.session.phoneNumber
        binding.ProfileBooksRead.text = userSession.session.booksRead.toString()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.NavigationHome -> {
                    val intent = Intent(this@ProfilePage, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.NavigationSearch -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.NavigationProfile -> {

                    true
                }
                else -> false
            }
        }

        binding.ButtonLogOut.setOnClickListener(this)
    }

    private fun passMaker(): String {
        val pass = userSession.session.password
        if (pass.isNotEmpty()) {
            return pass[0] + "*".repeat(pass.length - 1)
        }
        return ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                val intent = Intent(this@ProfilePage, MainActivity::class.java)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == binding.ButtonLogOut){
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val databaseReference = firebaseDatabase.getReference("userSession")

            val user = User("", "", "", 0)

            databaseReference.setValue(user)

            val intent = Intent(this@ProfilePage, RegisterPage::class.java)
            startActivity(intent)
        }
    }
}