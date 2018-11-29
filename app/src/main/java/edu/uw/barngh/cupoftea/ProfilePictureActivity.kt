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
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task

import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import com.google.firebase.storage.UploadTask


class ProfilePictureActivity : AppCompatActivity() {
    private val CAMERA_RESPONSE_CODE = 1

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageData: Intent?) {
        if (requestCode == CAMERA_RESPONSE_CODE && resultCode == Activity.RESULT_OK) {

            // Create a storage reference from our app
            val storageRef = storage.reference

            val filename = "2062134059"
            val ref = storageRef.child("profile_pics/$filename.jpg")
//            val mountainImagesRef =

// While the file names are the same, the references point to different files
//            mountainsRef.name == mountainImagesRef.name    // true
//            mountainsRef.path == mountainImagesRef.path    // false


            // do some thing with data
            val bitmap = imageData!!.extras.get("data") as Bitmap



            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            val data = baos.toByteArray()

            val uploadTask = ref.putBytes(data)
                .addOnFailureListener {
                    // Handle unsuccessful uploads
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_LONG).show()
                }.addOnSuccessListener {
                    val imageView = findViewById<ImageView>(R.id.img_thumbnail)
                    imageView.setImageBitmap(bitmap)
                    Toast.makeText(this, "Upload Done", Toast.LENGTH_SHORT).show()
                }


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
                    // can put it in shared pref
                    Log.v("profile_pic", downloadUri.toString())
                } else {
                    // Handle failures
                    // ...
                }
            }





        }

        super.onActivityResult(requestCode, resultCode, imageData)
    }




}
