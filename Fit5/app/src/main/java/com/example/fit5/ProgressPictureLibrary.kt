package com.example.fit5

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID


class ProgressPictureLibrary : AppCompatActivity() {
    lateinit var imageView1: ImageView
    lateinit var refreshPagebtn :Button
    lateinit var clearLibrary: Button
    lateinit var addImageBtn: Button

    //globals
    var filePath : Uri? = null
    val PICK_IMG_REQUEST = 22
    val storage = FirebaseStorage.getInstance()
    val storageReference =  storage.reference
    val firestore = FirebaseFirestore.getInstance()
    val REQUEST_IMAGE_CAPTURE=1
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.currentUser
    val userId = firebaseUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_picture_library)

        //check if user has images saved already
        if (userId != null) {
            val storageReference = FirebaseStorage.getInstance().getReference("images/$userId")
            storageReference.listAll().addOnSuccessListener { listResult ->
                if (listResult.items.isNotEmpty()) {
                    // User has images, so populate them
                    populateImagesForCurrentUser()
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to list images for user: $userId", e)
            }
        }
        //typecast
        imageView1 = findViewById(R.id.imageView1)
        addImageBtn = findViewById(R.id.AddImagebtn)
        refreshPagebtn = findViewById(R.id.refreshPagebtn)
        clearLibrary = findViewById(R.id.resetLibrary)

        //btn to add image
        addImageBtn.setOnClickListener {


                selectImage()

            
        }
        //refresh the page
        refreshPagebtn.setOnClickListener {
            finish()
            startActivity(intent)
        }
        //user can selete all the photos in their library
        clearLibrary.setOnClickListener {
            //get user id to delete the correct image folder
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$userId")

            storageRef.listAll()
                .addOnSuccessListener { listResult ->
                    // Delete each image in the folder
                    listResult.items.forEach { imageRef ->
                        imageRef.delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Image Library cleared", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show()
                                Log.e(TAG, "Error deleting image", exception)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    Log.e(TAG, "Error listing images", exception)
                }
        }
    }
    //select image from gallery
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMG_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMG_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            //check how many images a user has saved


            filePath = data.data
            try {
                //bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView1.setImageBitmap(bitmap)
                //save image to firebase


                    saveImageToFirebase(bitmap)

                    Toast.makeText(this, "refresh page", Toast.LENGTH_SHORT).show()


            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

                val imageBitmap = data?.extras?.get("data") as Bitmap
                //have the image display in the first ImageView
                imageView1.setImageBitmap(imageBitmap)
                saveImageToFirebase(imageBitmap)





        }
    }


//save image to firebase
    private fun saveImageToFirebase(imageBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        //save image with a unique id and add the .jpg extention
        val imageName = UUID.randomUUID().toString() + ".jpg"
    //get current user id
        val userId = firebaseUser?.uid

        if (userId != null) {
            //within the image folder create a new storage folder named after the current users ID
            val imageRef = storageReference.child("images/$userId/${UUID.randomUUID()}.jpg")

            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageURL = uri.toString()
                    // Save the imageURL to your database or perform any other operations
                }.addOnFailureListener {
                    Toast.makeText(this, "failed to save image", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "failed to save image", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun populateImagesForCurrentUser() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val userId = firebaseUser?.uid

        // Check if user is authenticated
        if (userId != null) {
            // Access the storage reference for the user's images
            val storageReference = FirebaseStorage.getInstance().getReference("images/$userId")

            // Get a list of all images in the user's folder
            storageReference.listAll().addOnSuccessListener { listResult ->
                listResult.items.forEachIndexed { index, item ->
                    if (index < 10) {
                        // Download each image and populate the corresponding ImageView
                        val imageViewId = resources.getIdentifier("imageView${index + 1}", "id", packageName)
                        val imageView = findViewById<ImageView>(imageViewId)

                        // Download the image bytes and create a Bitmap
                        item.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            imageView.setImageBitmap(bitmap)
                        }.addOnFailureListener { e ->
                            Log.e(TAG, "Failed to download image: ${item.name}", e)
                        }
                    }

                }
    }

}}}