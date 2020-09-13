package com.fhjoanneum.chatty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.chat_row_from.view.*
import kotlinx.android.synthetic.main.chat_row_to.view.*
import java.util.*


class ChatLogActivity : AppCompatActivity() {
    companion object{
        val TAG="ChatLog"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)


        val useridFROM=intent.getStringExtra(NewMessageActivity.USER_NAME)
        Log.d("Chat Log", "parced userid = $useridFROM")



    //showcase
        val adapter=GroupAdapter<ViewHolder>()
        adapter.add(ChatItemTo("Hey wie gehts"))
        adapter.add(ChatItemFrom("Ganz gut dir"))
        adapter.add(ChatItemTo("Ja bissi viel Stress"))
        adapter.add(ChatItemFrom("Gut dann bis bald"))
        messagesContainer.adapter=adapter


        //get data from user:FROM
        val db = Firebase.firestore
        val docRef = db.collection("users").document(useridFROM!!)
        val userName="User Name"
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    //get Username
                    val userName=document.get("username")
                    Log.d("test", "db username = $userName")
                    supportActionBar?.title= userName.toString()
                    //get User profile picture
                    val image=document.get("profileImageUrl")
                    Log.d("test", "db image = $image")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }




        setUpData()
        sendButton.setOnClickListener{
            Log.d("ChatLog","Attempt to send message")
            sendMessage(useridFROM)
        }

    }
    fun setUpData(){

        listenForMessages()
    }
    fun sendMessage(toId:String){
        val db = Firebase.firestore
        //get string from message from textView
        val text=enterMessage.text.toString()
        val myId= FirebaseAuth.getInstance().uid ?: return

        //create chatMessage
        val chatMessage=ChatMessage(text,myId,toId,System.currentTimeMillis())
        Log.d("Test","Message= $chatMessage")




        //save message to db
        db.collection("messages").document()
            .set(chatMessage)
                .addOnSuccessListener {
                    Log.d("ChatLog","Message was saved ${chatMessage}")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error saving message", e) }





    }
}
    private fun listenForMessages(){
        val db = Firebase.firestore
        val docRef = db.collection("messages").document()
        val ref = db.collection("messages")



    }
