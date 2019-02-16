package com.whats_app.kotlinwhatsapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.whats_app.kotlinwhatsapp.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_settings.*

class ProfileActivity : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mUserDatabase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getDataIntent()
    }

    private fun getDataIntent() {

        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            userId = intent.extras.get("userId").toString()
            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                .child(userId)

            setUpProfile()
        }
    }

    private fun setUpProfile() {

        mUserDatabase!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var displayName = dataSnapshot!!.child("display_name").value.toString()
                var status = dataSnapshot!!.child("status").value.toString()
                var image = dataSnapshot!!.child("image").value.toString()


                profileName.text = displayName
                profileStatus.text = status
                Glide.with(applicationContext)
                    .load(image)
                    .apply(RequestOptions().placeholder(R.drawable.profile_img))
                    .into(profilePic)

            }

            override fun onCancelled(p0: DatabaseError?) {
            }


        })
    }
}
