package com.example.dogwalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_btn.setOnClickListener {
            registerFun()
        }

        login_text_view.setOnClickListener {
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
        }

    }

    private fun registerFun(){
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter Email/Password",Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Register","Password is: $password")
        Log.d("Register","Email is: $email")

        //Firebase auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
                else {
                    Log.d("Register", "successfully added account uid: ${it.result?.user?.uid}")
                    saveUserToDatabase()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
            }
    }

    private fun saveUserToDatabase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, name_register.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Saved user to database")

                val intent = Intent(this,MainMenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
    }

}

class User(val uid: String, val name: String){
    constructor() : this("","")
}
