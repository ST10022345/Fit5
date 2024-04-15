package com.example.fit5.ui.exerciseViews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.fit5.MainActivity
import com.example.fit5.R
import com.example.fit5.databinding.ActivityBackExerciseViewBinding
import com.example.fit5.databinding.ActivityChestExerciseViewBinding

class BackExerciseView : AppCompatActivity() {
    private var binding: ActivityBackExerciseViewBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackExerciseViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExerciseBack)

        //back btn
        if (supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseListBack()
        binding?.toolBarExerciseBack?.setNavigationOnClickListener {
            onBackPressed()
        }
        setupRestView()
    }

    private fun setupRestView(){
        binding?.flProgressBarRest?.visibility = View.VISIBLE
        binding?.tvTitleBack?.visibility = View.VISIBLE
        binding?.tvExerciseBack?.visibility = View.INVISIBLE
        binding?.flExerciseBack?.visibility = View.INVISIBLE
        binding?.ivImageBack?.visibility = View.INVISIBLE
        if (restTimer!=null){
            restTimer?.cancel()
            restProgress = 0

        }

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBarRest?.visibility = View.INVISIBLE
        binding?.tvTitleBack?.visibility = View.INVISIBLE
        binding?.tvExerciseBack?.visibility = View.VISIBLE
        binding?.flExerciseBack?.visibility = View.VISIBLE
        binding?.ivImageBack?.visibility = View.VISIBLE
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding?.ivImageBack?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseBack?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        restProgress = 0 // Reset progress to 0 before starting the timer
        binding?.progressBarBack?.progress = 0 // Set progress bar to 0 initially

        restTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBarBack?.progress = restProgress // Update progress bar with the current progress
                binding?.tvTimerBack?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {

        binding?.progressBarExerciseBack?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExerciseBack?.progress = 30 - exerciseProgress // Update progress bar with the current progress
                binding?.tvTimerExerciseBack?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setupRestView()
                }else{
                    Toast.makeText(this@BackExerciseView, "Congratulations! you have completed the workout", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@BackExerciseView, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer!=null){
            restTimer?.cancel()
            restProgress = 0

        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding = null
    }
}