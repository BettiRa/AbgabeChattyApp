package com.fhjoanneum.chatty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.userlist_newmessage.*
import kotlinx.android.synthetic.main.userlist_newmessage.view.*


class NewMessageActivity : AppCompatActivity() {
    private lateinit var database2: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        userName_newMessage
        //TITLE: whats written on top bar
        supportActionBar?.title = "Select User"

        fetchUsers()

    }

    companion object{
        val USER_NAME="USERNAME"
    }


    //get users from db
    private fun fetchUsers() {
        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
        val adapter =GroupAdapter<ViewHolder>() //github implementation //empty constructor //no objects inside


        db.collection("users")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("test", "Listen failed.", e)
                    return@addSnapshotListener
                }
                //save users in List and parse it to UserItem3
                val users = ArrayList<String>()

                for (doc in value!!) {
                    //values of user
                    val name=doc.getString("username")
                    val id=doc.id
                    val image=doc.getString("profileImageUrl")
                    Log.d("test", "name = $name")
                    Log.d("test", "id = $id")
                    Log.d("test", "image = $image")

                    doc.getString("username")?.let {
                        users.add(it) //list
                        adapter.add(UserItem3(id,name!!,image!!)) //userItem
                    }
                }

                //when u click on an item
                adapter.setOnItemClickListener { item, view -> //item=row
                    Log.d("test", "item = $item")
                    val userItem= item as UserItem3  //get "From" User
                    Log.d("test", "userItem = $userItem")
                    val username=userItem.getNameUser()
                    Log.d("test", "username = $username")
                    val id=userItem.getIdUser()
                    Log.d("test", "userid = $id")

                    //start new activity
                    val intent = Intent(view.context,ChatLogActivity::class.java)
                   //works intent.putExtra(USER_NAME, userItem.getNameUser()) //send this to new activity
                    intent.putExtra(USER_NAME, userItem.getIdUser())
                    ///Log.d("test", "wanting to parce this = ${userItem.getNameUser()}")
                    Log.d("test", "wanting to parce this = ${userItem.getIdUser()}")
                    startActivity(intent)
                    finish() //finishes this activity
                }



                Log.d("test", "Current users: $users")
                recyclerview_newmessage.adapter = adapter
            }
    }

}





//only for showcase
class UserItem(): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.userlist_newmessage
    }
}


//actual UserItem
class UserItem3(val id:String, val name:String,val image:String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.userName_newMessage.text = name
        Picasso.get().load(image).into(viewHolder.itemView.profilePicture_newMessage)

    }

    override fun getLayout(): Int {
        return R.layout.userlist_newmessage
    }
    fun getNameUser():String{
        return name
    }
    fun getIdUser():String{
        return id
    }
    fun getImageUser():String{
        return image
    }
}