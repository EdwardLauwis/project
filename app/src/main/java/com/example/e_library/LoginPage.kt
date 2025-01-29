package com.example.e_library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.e_library.databinding.ActivityLoginPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginPage : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        binding= ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.ButtonLogin.setOnClickListener(this)
        binding.TextViewBackToRegisterPage.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        if (p0 == binding.ButtonLogin){
            val databaseReference: DatabaseReference = firebaseDatabase.getReference("Users")

            val username: String = binding.EditTextUsername.text.toString()
            val password: String = binding.EditTextPassword.text.toString()

            if (username.isEmpty()){
                binding.EditTextUsername.error = "Cannot be Empty"
                return
            }

            if (password.isEmpty()){
                binding.EditTextPassword.error = "Cannot be Empty"
                return
            }

            var user: User
            fun CheckUsername(callback: (Boolean) -> Unit){
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var usernameDataExist =  false
                        for (childSnapshot in snapshot.children){
                            if (childSnapshot.child("username").getValue(String::class.java).equals(username) && childSnapshot.child("password").getValue(String::class.java).equals(password)){
                                val name: String = childSnapshot.child("username").getValue(String::class.java).toString()
                                val pass: String = childSnapshot.child("password").getValue(String::class.java).toString()
                                val phoneNumber: String = childSnapshot.child("phoneNumber").getValue(String::class.java).toString()
                                val booksRead: Int? = childSnapshot.child("booksRead").getValue(Int::class.java)?.toInt()

                                usernameDataExist = true
                                user = booksRead?.let { User(name, pass, phoneNumber, it) }!!
                                userSession.session = user
                                break
                            }
                        }
                        callback(usernameDataExist)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        callback(false)
                    }
                })
            }

            CheckUsername() { exist ->
                if (exist) {
                    val connection = firebaseDatabase.getReference("userSession")
                    connection.setValue(userSession.session)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Does not exist", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (p0 == binding.TextViewBackToRegisterPage){
            val intent = Intent(this@LoginPage, RegisterPage::class.java)
            startActivity(intent)
        }
    }
}