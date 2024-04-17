package com.example.fit5

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
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
    lateinit var capButton : Button
    lateinit var edName: EditText
    lateinit var edDesc: EditText
    lateinit var startDateBtn: Button
    lateinit var startTimeBtn:Button
    lateinit var endDateBtn: Button
    lateinit var endTimeBtn:Button
    lateinit var takePicBtn:Button

    lateinit var imageViewPick: ImageView
    lateinit var database: DatabaseReference
    //globals
    var startDate: Date?=null
    var startTime: Date?=null
    var endDate: Date?=null
    var endTime:Date?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_routine)

        edName = findViewById(R.id.editTextText)
        edDesc = findViewById(R.id.editTextText2)
        spinner = findViewById(R.id.spinner)
        startDateBtn = findViewById(R.id.btnStartDate)
        startTimeBtn = findViewById(R.id.btnstartTime)
        endDateBtn = findViewById(R.id.btnEndDate)
        endTimeBtn = findViewById(R.id.BtnEndTime)
        capButton = findViewById(R.id.BtnCapture)

        imageViewPick = findViewById(R.id.imImageWorkout)
        takePicBtn = findViewById(R.id.btnTakePic)

        takePicBtn.setOnClickListener {
            openCamera()
        }

        database = FirebaseDatabase.getInstance().reference
        startTime = Calendar.getInstance().time

        //spinner
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter = spinnerAdapter
        // btn pull
        startDateBtn.setOnClickListener { showDate(startDateListener) }
        startTimeBtn.setOnClickListener { showTimePicker(startTimeListener) }
        endDateBtn.setOnClickListener { showDate(endDateListener) }
        endTimeBtn.setOnClickListener { showTimePicker(endTimeListener) }

        //firebase
        capButton.setOnClickListener {
            val selectedItem = spinner.selectedItem as String
            val taskName = edName.text.toString()
            val taskDesc = edDesc.text.toString()

            if(taskName.isEmpty()){
                edName.error = "Please enter a name"
                return@setOnClickListener

            }
            if (taskDesc.isEmpty()){
                edDesc.error = "please enter a description"
                return@setOnClickListener
            }
            saveToFirebase(selectedItem,taskName,taskDesc,selectedItem)
        }


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
            // Call save to Firebase method
            saveImageToFirebase(imageBitmap)
        }
    }

    private fun saveImageToFirebase(imageBitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        // Firebase
        val databaseReference = FirebaseDatabase.getInstance().getReference("images")
        val imgId = databaseReference.push().key
        // Subfolder
        if (imgId != null) {
            databaseReference.child(imgId).setValue(base64Image)
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 100
    }//end of take image code


    //end time listener
    val endTimeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay:Int, minute:Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.time = endDate!!
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        endTime = selectedCalendar.time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedTimeString = timeFormat.format(endTime!!)
        endTimeBtn.text = selectedTimeString
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
    //format --> fb --> date --> date util
    val startDateListener = DatePickerDialog.OnDateSetListener { _:DatePicker, year:Int, month:Int, day:Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year,month,day)
        startDate = selectedCalendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(startDate!!)
        startDateBtn.text = selectedDateString


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
    fun saveToFirebase(item:String, taskName: String,taskDesc: String,categoryString: String){
        //format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        //fetch the values from the local btns text
        val startDateString = startDateBtn.text.toString()
        val startTimeString = startTimeBtn.text.toString()
        val endDateString = endDateBtn.text.toString()
        val endTimeString = endTimeBtn.text.toString()
        val categoryString = spinner.selectedItem.toString()
        //parse values for firebase
        val startDate = dateFormat.parse(startDateString)
        val startTime = timeFormat.parse(startTimeString)
        val endDate = dateFormat.parse(endDateString)
        val endTime = timeFormat.parse(endTimeString)


        //calcs optional
        val totalTimeInMillis = endDate.time - startDate.time + endTime.time - startTime.time
        val totalMinutes = totalTimeInMillis / (1000*60)
        val totalHours = totalMinutes / 60
        val minutesRemaining = totalMinutes % 60
        val totalTimeString = String.format(Locale.getDefault(),
            "%02d:%02d", totalHours,minutesRemaining)


        val key = database.child("items").push().key
        if (key != null){
            val task = TaskModel(
                taskName,taskDesc,startDateString,startTimeString,endDateString,endTimeString,totalTimeString,categoryString)
            database.child("items").child(key).setValue(task)
                .addOnSuccessListener {
                    Toast.makeText(this, "Workout Routine saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                        err ->
                    Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
                    }
            }
}
}

//class ends
data class TaskModel(
    var taskName: String? = null,
    var taskDesc: String? = null,
    var startDateString: String? = null,
    var startTimeString: String? = null,
    var endDateString: String? = null,
    var endTimeString: String? = null,
    var totalTimeString: String? = null,
    var categoryString: String?=null
)