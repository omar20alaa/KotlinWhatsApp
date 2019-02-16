package com.whats_app.kotlinwhatsapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.whats_app.kotlinwhatsapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // variables
    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intFirebase()
        initClicks()
    }

    private fun intFirebase() {

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->

            user = firebaseAuth.currentUser

            if (user != null) {
                startActivity(Intent(this, dashBoardActivity::class.java))
                finish()
                Log.i("Message", "already login ")

            } else {
                Toast.makeText(this, "Not signed in", Toast.LENGTH_LONG).show()
                Log.i("Message", "not login yet")

            }

        }
    }

    private fun initClicks() {
        btn_have_account.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

        }

        btn_need_account.setOnClickListener {
            startActivity(Intent(this, CreateAccount::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()

        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }
}
