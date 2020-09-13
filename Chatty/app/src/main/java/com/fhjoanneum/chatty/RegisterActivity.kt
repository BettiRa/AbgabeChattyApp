package com.fhjoanneum.chatty

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lateinit var auth: FirebaseAuth///
        auth = Firebase.auth///
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser


        setContentView(R.layout.activity_main)

//REGISTER
        //on click register button: create user
        registerButton.setOnClickListener {
            performRegistration()
        }

        profilepicRegister.setOnClickListener{
            Log.d("RegisterActivity","Show Photo Selector")
            val intent= Intent(Intent.ACTION_PICK) //create intent
            intent.type="image/*"  //what type it is
            startActivityForResult(intent, 0) //want result from picking photo
        }


//LOGIN
        //already have an account button?: Go to Login Window: start loginactivity
        alreadyHaveAcc.setOnClickListener {
            //show activity_login screen when clicking button
            val intent= Intent(this, LoginActivity::class.java) //create intent
            startActivity(intent) //launch activity

        }





}

    private fun performRegistration(){
        //get  authentication input
        val email=mailAuth.text.toString()
        val password=passwordAuth.text.toString()

        //if empty, dont make it crash, make pop up
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all your information", Toast.LENGTH_SHORT).show()
            return
        }
        //make test log of mail & pw
        Log.d("RegisterActivity","email is:" + email)
        Log.d("RegisterActivity","pw is:" + password)
        //firebase: create and store user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener //wasnt successfull?

                Log.d("RegisterActivity", "Created user with User-ID successfully: ${it.result!!.user!!.uid}")//else if successfull
                //upload profile pic to Firebase Storage
                uploadPictureToFirebaseStorage()
            }
            //if it goes wrong
            .addOnFailureListener{
                Log.d("RegisterActivity","Failed to create user :${it.message}")
                Toast.makeText(this, "Failed to create User", Toast.LENGTH_SHORT).show()

            }
    }
     var selectedProfilePicURI: Uri?=null  //where image is stored on device  //is optional  //default=null
    //capture result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)  //get notified with the data of the intent
        if (requestCode==0 && resultCode== Activity.RESULT_OK && data != null){
            //proceed + check what image was
            Log.d("RegisterActivity","Photo was selected")

            selectedProfilePicURI=data.data
            //set circle Image View
            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,selectedProfilePicURI) //get access to bitmap
            circleImageView.setImageBitmap(bitmap)
            profilepicRegister.setVisibility(View.GONE)

        }
    }


   private fun uploadPictureToFirebaseStorage(){
        //if no pic was selected
        if (selectedProfilePicURI==null) return

        val filename = UUID.randomUUID().toString() //unique ID
        val reference= FirebaseStorage.getInstance().getReference("/images/$filename")

        reference.putFile(selectedProfilePicURI!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Successfully uploaded Profile Pic: ${it.metadata?.path}")

                //access file location
                reference.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File Location: $it")
                    saveUserToFirestore(it.toString()) //pass URL from PB
                }
            }
            .addOnFailureListener{
                Log.d("RegisterActivity","Failed to upload")
            }
    }
    private fun saveUserToFirestore(profileImageUrl: String){

        //add user with unique ID
        val user=User(usernameAuth.text.toString(),profileImageUrl)
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("RegisterActivity", "User is saved to Firestore with ID: ${documentReference.id}")
                //start latest message activity
                val intent=Intent(this,LatestMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //go back is not possible
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.w("RegisterActivity", "Error adding user", e)
            }


    }


}

