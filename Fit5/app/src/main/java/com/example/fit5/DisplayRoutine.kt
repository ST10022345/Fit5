package com.example.fit5

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



    class DisplayRoutine : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_display_routine)

            val listView = findViewById<ListView>(R.id.UserRoutines)

            //empty list of string
            val tasks = mutableListOf<String>()

            val database = FirebaseDatabase.getInstance().reference // Initialize the database reference


            database.child("items").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    val userId = firebaseUser?.uid

                    for (taskSnapshot in snapshot.children) {
                        val taskUserId = taskSnapshot.child("userID").getValue(String::class.java)

                        Log.e(TAG," taskuserID $taskUserId")//test to ensure task user ID is set
                        Log.e(TAG," currUserID $userId")//test to ensure current user ID is set
                        if (userId == taskUserId) {
                            val task = taskSnapshot.getValue(TaskModel::class.java)
                            val taskName = taskSnapshot.child("taskName").getValue(String::class.java)
                           taskName?.let { tasks.add(it) } // Add task name to the list if it's not null
                        }



                        //create onclicks for each item
                        listView.setOnItemClickListener { parent, view, position, id ->
                            val selectedTaskName = tasks[position]

                            // Find the corresponding task in the database
                            database.child("items").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.children.forEach { taskSnapshot ->
                                        val taskName = taskSnapshot.child("taskName").getValue(String::class.java)
                                        if (taskName == selectedTaskName) {
                                            // Found the matching task in the database
                                            val task = taskSnapshot.getValue(TaskModel::class.java)
                                            task?.let {
                                                // Display the task details in a dialog
                                                val dialogBuilder = AlertDialog.Builder(this@DisplayRoutine)
                                                dialogBuilder.setTitle("Task Details")
                                                dialogBuilder.setMessage(
                                                    "Task Name: ${it.taskName}\n" +
                                                            "Description: ${it.taskDesc}\n" +
                                                            "Start Date: ${it.startDateString}\n" +
                                                            "Start Time: ${it.startTimeString}\n" +
                                                            "End Date: ${it.endDateString}\n" +
                                                            "End Time: ${it.endTimeString}\n" +
                                                            "Total Time: ${it.totalTimeString}\n" +
                                                            "Category: ${it.categoryString}\n"

                                                )

                                                // Load the image from the base64 string and display it
                                                if (!it.imageString.isNullOrEmpty()) {
                                                    val decodedString = Base64.decode(it.imageString, Base64.DEFAULT)
                                                    val decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                                    val imageView = ImageView(this@DisplayRoutine)
                                                    imageView.setImageBitmap(decodedBitmap)
                                                    dialogBuilder.setView(imageView)
                                                }
                                                dialogBuilder.setPositiveButton("OK") { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                val dialog = dialogBuilder.create()
                                                dialog.show()
                                            }
                                            return@forEach // Exit the loop after finding the matching task
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.e(TAG, "Failed to read value.", error.toException())
                                }
                            })
                        }




                    }

                    val adapter = ArrayAdapter(this@DisplayRoutine, android.R.layout.simple_list_item_1, tasks)
                    listView.adapter = adapter
                }



                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read value.", error.toException())
                }
            })

        }
    }







