package com.whats_app.kotlinwhatsapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.whats_app.kotlinwhatsapp.R
import com.whats_app.kotlinwhatsapp.models.FriendlyMessage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.custom_bar_image.view.*

class ChatActivity : AppCompatActivity() {

    var userId: String? = null
    var mFirebaseDatabaseRef: DatabaseReference? = null
    var mFirebaseUser: FirebaseUser? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseRecyclerAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initFirebase()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        chat_recycler_view.layoutManager = mLinearLayoutManager
        chat_recycler_view.adapter = mFirebaseRecyclerAdapter

        sendButton.setOnClickListener {
            if (!intent.extras.get("name").toString().equals("")) {

                var currentUsername = intent.extras.get("name")
                var mCurrentUserId = mFirebaseUser!!.uid

                var friendlyMessage = FriendlyMessage(
                    mCurrentUserId.toString(), messageEdt.text.toString().trim()
                    , currentUsername.toString().trim()
                )

                mFirebaseDatabaseRef!!.child("messages")
                    .push().setValue(friendlyMessage)

            }
        }
    }

    private fun initFirebase() {
        mFirebaseUser = FirebaseAuth.getInstance().currentUser

        userId = intent.extras.getString("userId")
        var profileImgLink = intent.extras.get("profile").toString()
        mLinearLayoutManager = LinearLayoutManager(applicationContext)
        mLinearLayoutManager!!.stackFromEnd = true
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowCustomEnabled(true)

        var inflater = this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater

        var actionBarView = inflater.inflate(R.layout.custom_bar_image, null)
        actionBarView.customBarName.text = intent.extras.getString("name")

        Glide.with(applicationContext)
            .load(profileImgLink)
            .apply(RequestOptions().placeholder(R.drawable.profile_img))
            .into(actionBarView.customBarCircleImage)

        supportActionBar!!.customView = actionBarView

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        mFirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage,
                MessageViewHolder>(
            FriendlyMessage::class.java,
            R.layout.item_message,
            MessageViewHolder::class.java,
            mFirebaseDatabaseRef!!.child("messages")
        ) {

            override fun populateViewHolder(
                holder: MessageViewHolder?,
                friendlyMessage: FriendlyMessage,
                position: Int
            ) {

                if (friendlyMessage!!.text != null) {
                    holder!!.bindView(friendlyMessage)

                    var currentUserId = mFirebaseUser!!.uid
                    var isMe: Boolean = friendlyMessage!!.id.equals(currentUserId)

                    if (isMe) {
                        holder.profileImageViewRight!!.visibility = View.VISIBLE
                        holder.profileImageView!!.visibility = View.GONE
                        holder.messageTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)
                        holder.messengerTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)

                        mFirebaseDatabaseRef!!.child("Users")
                            .child(currentUserId)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(data: DataSnapshot?) {

                                    var imageUrl = data!!.child("thumb_image").value.toString()
                                    var displayName = data!!.child("display_name").value

                                    holder.messengerTextView!!.text =
                                        "${displayName} wrote..."

                                    Glide.with(holder.profileImageView!!.context)
                                        .load(imageUrl)
                                        .apply(RequestOptions().placeholder(R.drawable.profile_img))
                                        .into(holder.profileImageViewRight!!)

                                }


                                override fun onCancelled(p0: DatabaseError?) {

                                }

                            })

                    } else {
                        // the other person show imageview to left side

                    }
                }
            }
        }

    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTextView: TextView? = null
        var messengerTextView: TextView? = null
        var profileImageView: CircleImageView? = null
        var profileImageViewRight: CircleImageView? = null

        fun bindView(friendlyMessage: FriendlyMessage) {
            messageTextView = itemView.findViewById(R.id.messageTextView)
            messengerTextView = itemView.findViewById(R.id.messangerTextView)
            profileImageView = itemView.findViewById(R.id.messangerImageView)
            profileImageViewRight = itemView.findViewById(R.id.profileImageViewRight)

            messageTextView!!.text = friendlyMessage.text
            messengerTextView!!.text = friendlyMessage.name
        }
    }


}
