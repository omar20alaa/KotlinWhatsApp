package com.whats_app.kotlinwhatsapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.whats_app.kotlinwhatsapp.R
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccount : AppCompatActivity() {

    // variables
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mAuth = FirebaseAuth.getInstance()
        register()
    }

    fun register() {

        btn_create_new_account.setOnClickListener {

            var name = et_name.text.toString().trim()
            var email = et_email.text.toString().trim()
            var password = et_password.text.toString().trim()

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                createAccount(name, email, password)
            } else {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_LONG).show()
            }
        }

    }


    fun createAccount(displayName: String, email: String, password: String) {

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->

                if (task.isSuccessful) {
                    var current_user = mAuth!!.currentUser
                    var userId = current_user!!.uid

                    mDatabase = FirebaseDatabase.getInstance().reference
                        .child("Users").child(userId)


                    var userObject = HashMap<String, String>()
                    userObject.put("display_name", displayName)
                    userObject.put("status", "Hello there ...")
                    userObject.put("image", "default")
                    userObject.put("thumb_image", "default")

                    mDatabase!!.setValue(userObject).addOnCompleteListener {

                            task: Task<Void> ->
                        if (task.isSuccessful) {
                            var dashboardIntent = Intent(this, dashBoardActivity::class.java)
                            dashboardIntent.putExtra("name", displayName)
                            startActivity(dashboardIntent)
                            finish()
                            Toast.makeText(this, "User created ", Toast.LENGTH_LONG)
                            Log.i("message", "user created")
                        } else {
                            Toast.makeText(this, "User not created ", Toast.LENGTH_LONG)
                            Log.i("message", "user not created")

                        }
                    }


                }

            }

    }
}
