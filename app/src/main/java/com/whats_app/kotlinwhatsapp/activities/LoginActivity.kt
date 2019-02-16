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
import com.google.firebase.database.*
import com.whats_app.kotlinwhatsapp.R
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {


    // variables
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        mAuth = FirebaseAuth.getInstance()
        initLogin()

    }

    private fun initLogin() {
        btn_login.setOnClickListener {

            var email = et_email.text.toString().trim()
            var password = et_password.text.toString().trim()

            Log.i("Message" , "email : " + email)
            Log.i("Message" , "password : " + password)

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                loginUser(email, password)
                Log.i("Message" , "success : ")

            }
            else {
                Toast.makeText(this, "Sorry , login failed ...", Toast.LENGTH_LONG).show()
                Log.i("Message" , "failed : ")

            }
        }
    } // login function

    private fun loginUser(email: String, password: String) {

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    var username = email.split("@")[0]

                    var dashboardIntent = Intent(this, dashBoardActivity::class.java)
                    dashboardIntent.putExtra("name", username)
                    startActivity(dashboardIntent)
                    finish()
                    Toast.makeText(this, "User created ", Toast.LENGTH_LONG)
                    Log.i("message", "user created")
                } else {
                    Toast.makeText(this, "Login failed ...", Toast.LENGTH_LONG).show()
                }

            }

    }

}
