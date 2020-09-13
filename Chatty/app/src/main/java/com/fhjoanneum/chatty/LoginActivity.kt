package com.fhjoanneum.chatty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) //show login screen


//on click button login: sign in user
        loginButton.setOnClickListener {
            performLogin()
        }
        //back to registration: go to mainActivity
        backToReg_login.setOnClickListener {
            val intent= Intent(this, RegisterActivity::class.java) //create intent
            startActivity(intent) //launch activity
        }
    }



    private fun performLogin(){
        //get login input
        val email=mailLogin.text.toString()
        val password=passwordLogin.text.toString()
        Log.d("Login","email login is:" + email)
        Log.d("Login","pw login is:" + password)
        //sign in user
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener //wasnt successfull?
                Log.d("Login", "signInWithEmail:success")
                val user = Firebase.auth.currentUser
                val intent=Intent(this,LatestMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //go back is not possible
                startActivity(intent)
            }
            //if it goes wrong
            .addOnFailureListener{
                Log.d("Login", "signInWithEmail:failure")
                Toast.makeText(this, "Unable to load User", Toast.LENGTH_SHORT).show()

            }
    }
}