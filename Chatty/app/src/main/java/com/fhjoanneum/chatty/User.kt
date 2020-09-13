package com.fhjoanneum.chatty

import android.content.ClipData
import android.content.Context
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.chat_row_from.view.*
import kotlinx.android.synthetic.main.chat_row_to.view.*
import kotlinx.android.synthetic.main.userlist_newmessage.*
import kotlinx.android.synthetic.main.userlist_newmessage.view.*

//user with infos
@Parcelize
class User(val username:String,val profileImageUrl:String) : Parcelable {
      constructor():this("","")
}
//left message
class ChatItemFrom(val text: String): Item<ViewHolder>(){
      override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.messageViewFrom.text=text
      }
      override fun getLayout(): Int {
            //left one, FROM person
            return R.layout.chat_row_from

      }
//right message
}class ChatItemTo(val text: String): Item<ViewHolder>(){
      override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.messageViewTo.text=text
      }
      override fun getLayout(): Int {
            //right one, TO person
            return R.layout.chat_row_to

      }

}

//full message
class ChatMessage( val text:String, val fromId:String, val toId:String,val timestamp:Long){
      constructor():this("","","",-1)
}
