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
import com.google.firebase.database.getValue

class LoginPage : AppCompatActivity(), OnClickListener {

    private lateinit var Binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        Binding= ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        supportActionBar?.hide()

        Binding.ButtonLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        if (p0 == Binding.ButtonLogin){
            val databaseReference: DatabaseReference = firebaseDatabase.getReference("Users")

            val username: String = Binding.EditTextUsername.text.toString()
            val password: String = Binding.EditTextPassword.text.toString()

            if (username.isEmpty()){
                Binding.EditTextUsername.error = "Cannot be Empty"
                return
            }

            if (password.isEmpty()){
                Binding.EditTextPassword.error = "Cannot be Empty"
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
                                val booksRead: Int = childSnapshot.child("phoneNumber").getValue(String::class.java)
                                    ?.toIntOrNull()
                                    ?: 0
                                println("ini dia$name$pass$phoneNumber$booksRead")

                                usernameDataExist = true
                                user = User(name, pass, phoneNumber, booksRead)
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
        }
    }
}