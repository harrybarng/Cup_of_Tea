package edu.uw.barngh.cupoftea

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.net.Uri
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task

import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import com.google.firebase.storage.UploadTask


class ProfilePictureActivity : AppCompatActivity() {
    private val CAMERA_RESPONSE_CODE = 1
    private val OPEN_DOCUMENT_CODE = 2

    private val storage = FirebaseStorage.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture)

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            val intent = Intent(this, AgeActivity::class.java)
            this.startActivity(intent)
        }

    }

    fun takePicture(v: View?) {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_RESPONSE_CODE)
        }

    }

    fun fromAlbum(v: View?) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_DOCUMENT_CODE)

    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageData: Intent?) {
        if (requestCode == CAMERA_RESPONSE_CODE || requestCode == OPEN_DOCUMENT_CODE && resultCode == Activity.RESULT_OK) {


            var bitmap = if (requestCode == CAMERA_RESPONSE_CODE) {
                if (imageData!!.extras == null) {
                    null
                } else {
                    imageData!!.extras.get("data") as Bitmap
                }

            } else {
                getResizedBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, imageData!!.data), 200)
            }
            if (bitmap != null) {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                val data = baos.toByteArray()

                // Create a storage reference from our app
                val storageRef = storage.reference

                val filename = "2062134059"
                val ref = storageRef.child("profile_pics/$filename.jpg")



                Toast.makeText(this, "Uploading....", Toast.LENGTH_SHORT).show()

                val uploadTask = ref.putBytes(data)
                    .addOnFailureListener {
                        // Handle unsuccessful uploads
                        Toast.makeText(this, "Upload Failed", Toast.LENGTH_LONG).show()
                    }
//                .addOnSuccessListener {
//
//                }


                val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            val settings = PreferenceManager.getDefaultSharedPreferences(this)
                            val editor = settings.edit()
                            editor.putString(getString(R.string.key_profile_picture), downloadUri.toString())
                            editor.apply()


                            val imageView = findViewById<ImageView>(R.id.img_thumbnail)
                            imageView.setImageBitmap(bitmap)
                            Toast.makeText(this, "Upload Done", Toast.LENGTH_SHORT).show()

    //                    Log.v("profile_pic", "Profile pic url from preference: " + settings.getString(R.string.key_profile_picture.toString(), ""))
                            // can put it in shared pref
    //                    Log.v("profile_pic", downloadUri.toString())

                        } else {
                            Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                            // Handle failures
                            // ...
                        }

                    }

                }
        }

            super.onActivityResult(requestCode, resultCode, imageData)

    }


}
