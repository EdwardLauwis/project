package com.example.e_library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.e_library.databinding.ActivityProfilePageBinding

class ProfilePage : AppCompatActivity() {
    private lateinit var binding: ActivityProfilePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        supportActionBar?.title = ""

        binding.ProfileUsername.text = userSession.session.username
        binding.ProfilePassword.text = passMaker()
        binding.ProfilePhoneNumber.text = userSession.session.phoneNumber
        binding.ProfileBooksRead.text = userSession.session.booksRead.toString()
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
}