package com.whats_app.kotlinwhatsapp.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.whats_app.kotlinwhatsapp.R
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_account.*


class SettingsActivity : AppCompatActivity() {

    // variables
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mStorageRef: StorageReference? = null
    var userId = ""
    var GALLERY_ID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initFirebase()
        initClicks()


    }


    private fun initFirebase() {
        mDatabase = FirebaseDatabase.getInstance().reference
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        mStorageRef = FirebaseStorage.getInstance().reference
        userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)


        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var displayName = dataSnapshot!!.child("display_name").value
                var image = dataSnapshot!!.child("image").value.toString()
                var userStatus = dataSnapshot!!.child("status").value
                var thumbnails = dataSnapshot!!.child("thumb_image").value

                profile_name.text = displayName.toString()
                profile_status.text = userStatus.toString()



                if (!image!!.equals("default")) {


                    Glide.with(applicationContext)
                        .load(image)
                        .apply(RequestOptions().placeholder(R.drawable.profile_img))
                        .into(profile_image)
                }

            }

        })

    }

    private fun initClicks() {

        btn_change_status.setOnClickListener {

            var intent = Intent(this, StatusActivity::class.java)
            intent.putExtra("status", profile_status.text.toString().trim())
            startActivity(intent)

        }

        btn_change_image.setOnClickListener {

            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.i("Message", "on activity result method : ")

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {

            var image: Uri = data!!.data

            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)

        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                var userId = mCurrentUser!!.uid
                var thumbFile = File(resultUri.path)

                var thumbBitMap = Compressor(this)
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(65)
                    .compressToBitmap(thumbFile)

                var byteArray = ByteArrayOutputStream()

                thumbBitMap.compress(
                    Bitmap.CompressFormat.JPEG, 100,
                    byteArray
                )
                var thumbByteArray: ByteArray
                thumbByteArray = byteArray.toByteArray()

                var filePath = mStorageRef!!.child("chat_profile_images")
                    .child(userId + ".jpg")

                var thumbFilePath = mStorageRef!!.child("chat_profile_images")
                    .child("thumbs")
                    .child(userId + ".jpg")

                filePath.putFile(resultUri)
                    .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                        if (task.isSuccessful) {

                            var downloadUrl = task.result.downloadUrl.toString()
                            task.result!!.uploadSessionUri.toString()

                            Log.i("Message", "downloadUrl : " + downloadUrl)

                            var uploadTask: UploadTask = thumbFilePath
                                .putBytes(thumbByteArray)

                            uploadTask.addOnCompleteListener {

                                    task: Task<UploadTask.TaskSnapshot> ->

                                var thumbUrl = task.result.downloadUrl.toString()

                                Log.i("Message", "thumbUrl : " + thumbUrl)

                                if (task.isSuccessful) {
                                    var updateObj = HashMap<String, Any>()
                                    updateObj.put("image", downloadUrl)
                                    updateObj.put("thumb_image", thumbUrl)

                                    mDatabase!!.updateChildren(updateObj)
                                        .addOnCompleteListener { task: Task<Void> ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(this, "Profile image updated ", Toast.LENGTH_LONG).show()
                                                Log.i("Message", "status : " + task.getResult())
                                            } else {
                                                Log.i("Message", "status : " + task.exception)
                                            }
                                        }
                                } else {
                                    Log.i("Message", "status : " + task.exception)

                                }
                            }

                        }
                    }
            }
        }

        //super.onActivityResult(requestCode, resultCode, data)
    }

}
