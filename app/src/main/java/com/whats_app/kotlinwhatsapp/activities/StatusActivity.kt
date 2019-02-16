package com.whats_app.kotlinwhatsapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.whats_app.kotlinwhatsapp.R
import kotlinx.android.synthetic.main.activity_status.*

class StatusActivity : AppCompatActivity() {

    // variables
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        getDataIntent()
        initClicks()
    }

    private fun initClicks() {

        btn_update_status.setOnClickListener {

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var userId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)

            var status = et_status.text.toString().trim()

            mDatabase!!.child("status")
                .setValue(status)
                .addOnCompleteListener { task: Task<Void> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Status updated successfully!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, SettingsActivity::class.java))
                    } else {
                        Toast.makeText(this, "Status not updated !", Toast.LENGTH_LONG).show()

                    }
                }

        }
    }

    private fun getDataIntent() {
        supportActionBar!!.title = "Status"

        if (intent.extras != null) {
            var oldStatus = intent.extras.get("status")
            Log.i("Message" , "status : " + oldStatus)
            et_status.setText(oldStatus.toString())
        }
        if (intent.extras.equals(null)) {

            et_status.setText("Enter your status ...")

        }
    }
}
