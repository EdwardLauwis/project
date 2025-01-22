package com.example.e_library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.e_library.databinding.ActivityRegisterPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterPage : AppCompatActivity(), OnClickListener {

    private lateinit var Binding: ActivityRegisterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        Binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        supportActionBar?.hide()

        Binding.ButtonRegister.setOnClickListener(this)
        Binding.TextViewLoginText.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 == Binding.ButtonRegister){
            val Username: String = Binding.EditTextUsername.text.toString()
            val Password: String = Binding.EditTextPassword.text.toString()
            val ConPassword: String = Binding.EditTextConPassword.text.toString()
            val PhoneNumber: String = Binding.EditTextPhoneNumber.text.toString()

            if (Username.isEmpty()){
                Binding.EditTextUsername.setError("Cannot be Empty")
                return
            }

            if (Password.isEmpty()){
                Binding.EditTextPassword.setError("Cannot be Empty")
                return
            }

            if (!ConPassword.equals(Password)){
                Binding.EditTextConPassword.setError("It's not match with Password")
                return
            }

            if (PhoneNumber.isEmpty()){
                Binding.EditTextPhoneNumber.setError("Cannot be empty")
                return
            }

            val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
            val databaseReference: DatabaseReference = firebaseDatabase.getReference("Users")

            fun checkUsername(username: String, callback: (Boolean) -> Unit){
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var usernameDataExist = false
                        for (childSnapshot in snapshot.children){
                            if (childSnapshot.child("username").getValue(String::class.java).equals(Username)){
                                usernameDataExist = true
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

            checkUsername(Username) { exist ->
                if (exist) {
                    Binding.EditTextUsername.setError("Username Already Taken")
                } else {
                    val user = User(Username, Password, PhoneNumber)

                    val newUserKey = databaseReference.push().key

                    if (newUserKey != null) {
                        databaseReference.child(newUserKey).setValue(user)

                        Binding.EditTextUsername.setText("")
                        Binding.EditTextPassword.setText("")
                        Binding.EditTextConPassword.setText("")
                        Binding.EditTextPhoneNumber.setText("")

                        userSession.session = user

                        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
                        val databaseReference: DatabaseReference = firebaseDatabase.getReference("userSession")

                        databaseReference.setValue(userSession.session)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Handle the case where the key generation failed
                        Toast.makeText(this, "Error generating user key", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (p0 == Binding.TextViewLoginText){
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}
