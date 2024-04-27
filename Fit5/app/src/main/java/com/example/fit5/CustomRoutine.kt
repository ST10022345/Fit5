package com.example.fit5

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CustomRoutine : AppCompatActivity() {
    //variables
    lateinit var spinner : Spinner
    lateinit var addCategoryBtn : Button
    lateinit var edName: EditText
    lateinit var edDesc: EditText
    lateinit var startDateBtn: Button
    lateinit var startTimeBtn:Button
    lateinit var endDateBtn: Button
    lateinit var endTimeBtn:Button
    lateinit var takePicBtn:Button
    lateinit var myRoutinesbtn: Button
    lateinit var customCategory : EditText
    lateinit var categories : ArrayList<String>
    lateinit var adapter : ArrayAdapter<String>
    lateinit var imageViewPick: ImageView
    lateinit var database: DatabaseReference
    //globals
    var startDate: Date?=null
    var startTime: Date?=null
    var endDate: Date?=null
    var endTime:Date?=null
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.currentUser
    val userId = firebaseUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_routine)
        //typecast
        edName = findViewById(R.id.editTextText)
        edDesc = findViewById(R.id.editTextText2)
        spinner = findViewById(R.id.spinner)
        startDateBtn = findViewById(R.id.btnStartDate)
        startTimeBtn = findViewById(R.id.btnstartTime)
        endDateBtn = findViewById(R.id.btnEndDate)
        endTimeBtn = findViewById(R.id.BtnEndTime)
        myRoutinesbtn = findViewById(R.id.MyRoutinesbtn)
        customCategory = findViewById(R.id.edCategory)
        addCategoryBtn = findViewById(R.id.addCategoryBtn)
        imageViewPick = findViewById(R.id.imImageWorkout)
        takePicBtn = findViewById(R.id.btnTakePic)

        // populate the spinner
        categories = ArrayList()
        //add 1 item to ensure all items have a category
        categories.add("Core")
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //add the item to the spinner
        addCategoryBtn.setOnClickListener {
            val newCategory = customCategory.text.toString()
            if(newCategory.isNotEmpty() && !categories.contains(newCategory)){
                categories.add(newCategory)
                adapter.notifyDataSetChanged()
                customCategory.text.clear()

            }else{
                //ensure user enters a category
                customCategory.error = "enter a new category"

            }
        }

        takePicBtn.setOnClickListener {
            //error handling
            val taskName = edName.text.toString().trim()
            val taskDesc = edDesc.text.toString().trim()
            val category = spinner.selectedItem.toString()

            if (startDate == null || startTime == null || endDate == null || endTime == null) {
                Toast.makeText(this, "Please select all dates and times", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (taskName.isEmpty() || taskDesc.isEmpty() || category.isEmpty() || startDate == null || startTime == null || endDate == null || endTime == null) {
                Toast.makeText(this, "Please fill in all fields and select all dates and times", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            try {
                openCamera()
            } catch (e: Exception) {
                // Handle any exceptions that occur while opening the camera

                Toast.makeText(this, "Error opening camera", Toast.LENGTH_SHORT).show()
            }
        }

        //navigate to view all list of times
        myRoutinesbtn.setOnClickListener {
            val  intent = Intent(this@CustomRoutine, DisplayRoutine::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance().reference
        startTime = Calendar.getInstance().time



        // btn pull dates and times
        startDateBtn.setOnClickListener { showDate(startDateListener) }
        startTimeBtn.setOnClickListener { showTimePicker(startTimeListener) }
        endDateBtn.setOnClickListener { showDate(endDateListener) }
        endTimeBtn.setOnClickListener { showTimePicker(endTimeListener) }




    }
//take image code
    private fun openCamera() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Display the image in ImageView
            imageViewPick.setImageBitmap(imageBitmap)

            val selectedItem = spinner.selectedItem as String
            val taskName = edName.text.toString()
            val taskDesc = edDesc.text.toString()

            if(taskName.isEmpty()){
                edName.error = "Please enter a name"
                return

            }
            if (taskDesc.isEmpty()){
                edDesc.error = "please enter a description"
                return
            }
            saveImageToFirebase(imageBitmap)
        }
    }

    //save everything to firebase

    private fun saveImageToFirebase(imageBitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

        //ensure user selects all dates and  times
        if (startDate == null || startTime == null || endDate == null || endTime == null) {
            Toast.makeText(this, "Please select all dates and times", Toast.LENGTH_SHORT).show()
            return
        }

        // Ensure user enters a task name and description
        val taskName = edName.text.toString().trim()
        val taskDesc = edDesc.text.toString().trim()
        if (taskName.isEmpty()) {
            edName.error = "Please enter a name"
            return
        }
        if (taskDesc.isEmpty()) {
            edDesc.error = "Please enter a description"
            return
        }


        //calcs
        val totalTimeInMillis = endDate!!.time - startDate!!.time + endTime!!.time - startTime!!.time
        val totalMinutes = totalTimeInMillis / (1000*60)
        val totalHours = totalMinutes / 60
        val minutesRemaining = totalMinutes % 60
        val totalTimeString = String.format(Locale.getDefault(),
            "%02d:%02d", totalHours,minutesRemaining)
        //get current userId
        val userId = firebaseUser?.uid

        //write to real time db
        val key = database.child("items").push().key
        if (key != null) {
            val task = TaskModel(
                taskName = edName.text.toString(),
                taskDesc = edDesc.text.toString(),
                startDateString = startDateBtn.text.toString(),
                startTimeString = startTimeBtn.text.toString(),
                endDateString = endDateBtn.text.toString(),
                endTimeString = endTimeBtn.text.toString(),
                totalTimeString = totalTimeString,
                categoryString = spinner.selectedItem.toString(),
                imageString = base64Image,
                userID = userId

            )




                database.child("items").child(key).setValue(task)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Workout Routine saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { err ->
                        Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
                    }


        }
    }


    companion object {
        const val CAMERA_REQUEST_CODE = 100
    }//end of take image code


    //end time listener
    val endTimeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay:Int, minute:Int ->
        val selectedCalendar = Calendar.getInstance()

        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        endTime = selectedCalendar.time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedTimeString = timeFormat.format(endTime!!)
        endTimeBtn.text = selectedTimeString
    }

    val startTimeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val selectedCalendar = Calendar.getInstance()

        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        startTime = selectedCalendar.time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedTimeString = timeFormat.format(startTime!!)
        startTimeBtn.text = selectedTimeString
    }



    //method to pick the date
    fun showDate(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener, year, month, day
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
        }
    fun showTimePicker(timeSetListener:TimePickerDialog.OnTimeSetListener){
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this,
            timeSetListener,hour,minute,true)
        timePickerDialog.show()
    }
    //start date
    val startDateListener = DatePickerDialog.OnDateSetListener { _:DatePicker, year:Int, month:Int, day:Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year,month,day)
        startDate = selectedCalendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(startDate!!)
        startDateBtn.text = selectedDateString


    }




    //end date
    val endDateListener = DatePickerDialog.OnDateSetListener { _: DatePicker,
                                                               year:Int, month:Int, day:Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year,month,day)
        endDate = selectedCalendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(endDate!!)
        endDateBtn.text = selectedDateString
    }
    //method for firebase





}

//class ends
data class TaskModel(
    var taskId: String? = null,
    var taskName: String? = null,
    var taskDesc: String? = null,
    var startDateString: String? = null,
    var startTimeString: String? = null,
    var endDateString: String? = null,
    var endTimeString: String? = null,
    var totalTimeString: String? = null,
    var categoryString: String?=null,
    var imageString: String? = null,
    var userID: String? = null
)