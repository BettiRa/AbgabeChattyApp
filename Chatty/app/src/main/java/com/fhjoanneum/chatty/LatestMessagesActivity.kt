package com.fhjoanneum.chatty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class LatestMessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        //check if logged in already
        isUserLoggedIn()
    }
    //check if logged in already or go to RegistrationActivity
    private fun isUserLoggedIn(){
        val uid=FirebaseAuth.getInstance().uid
        if(uid==null){
            val intent= Intent(this,RegisterActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //go back is not possible
            startActivity(intent)
        }
    }
    //if buttons on top get clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId ) {//get item ID: new message & sign out
            //if the button gets hit (switch case)
            R.id.menu_new_message->{
                //go to NewMessageActivity screen
                val intent=Intent(this,NewMessageActivity::class.java)
                startActivity(intent)
            }
            //sign out & go to Sign up
            R.id.menu_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(this,RegisterActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //go back is not possible
                startActivity(intent)
                finish()   //brings u back to LatestMessages
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}