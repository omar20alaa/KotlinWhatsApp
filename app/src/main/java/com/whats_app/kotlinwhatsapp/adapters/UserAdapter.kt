package com.whats_app.kotlinwhatsapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.whats_app.kotlinwhatsapp.R
import com.whats_app.kotlinwhatsapp.activities.ChatActivity
import com.whats_app.kotlinwhatsapp.activities.ProfileActivity
import com.whats_app.kotlinwhatsapp.models.Users
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(databaseQuery: DatabaseReference, var context: Context) : FirebaseRecyclerAdapter<Users,
        UserAdapter.ViewHolder>(
    Users::class.java,
    R.layout.users_row,
    UserAdapter.ViewHolder::class.java,
    databaseQuery
) {
    override fun populateViewHolder(holder: UserAdapter.ViewHolder, users: Users?, position: Int) {

        var userId = getRef(position).key // unique firebase key id for current user

        holder!!.bindView(users!!, context)

        holder.itemView.setOnClickListener {

            var options = arrayOf("Open profile", "Send message")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Select options")
            builder.setItems(options, DialogInterface.OnClickListener
            { dialogInterface, i ->

                var userName = holder.tvUserName
                var userStatus = holder.tvUserStatus
                var profilePic = holder.userProfilePicLink

                if (i == 0) {
                    // open user profile
                    var profile = Intent(context, ProfileActivity::class.java)
                    profile.putExtra("userId", userId)
                    context.startActivity(profile)
                } else {
                    // send message
                    var chat = Intent(context, ChatActivity::class.java)
                    chat.putExtra("userId", userId)
                    chat.putExtra("name", userName)
                    chat.putExtra("status", userStatus)
                    chat.putExtra("profile", profilePic)
                    context.startActivity(chat)
                }

            })
            builder.show()

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUserName: String? = null
        var tvUserStatus: String? = null
        var userProfilePicLink: String? = null

        fun bindView(user: Users, context: Context) {
            var userName = itemView.findViewById<TextView>(R.id.user_name)
            var userStatus = itemView.findViewById<TextView>(R.id.user_status)
            var userProfilePic = itemView.findViewById<CircleImageView>(R.id.user_image)


            tvUserName = user.display_name
            tvUserStatus = user.status
            userProfilePicLink = user.thumb_image

            userName.text = user.display_name
            userStatus.text = user.status

            Log.i("Message", "userName : " + userName.text)
            Log.i("Message", "userStatus : " + userStatus.text)

            Glide.with(context)
                .load(userProfilePicLink)
                .apply(RequestOptions().placeholder(R.drawable.profile_img))
                .into(userProfilePic)

        }
    }

}