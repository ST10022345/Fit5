package com.example.fit5.ui.exerciseViews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.fit5.MainActivity
import com.example.fit5.R
import com.example.fit5.databinding.ActivityCardioExerciseViewBinding
import com.example.fit5.databinding.ActivityChestExerciseViewBinding

class CardioExerciseView : AppCompatActivity() {
    private var binding: ActivityCardioExerciseViewBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardioExerciseViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExerciseCardio)

        //back btn
        if (supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseListCardio()
        binding?.toolBarExerciseCardio?.setNavigationOnClickListener {
            onBackPressed()
        }
        setupRestView()
    }

    private fun setupRestView(){
        binding?.flProgressBarRest?.visibility = View.VISIBLE
        binding?.tvTitleCardio?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseCardio?.visibility = View.VISIBLE
        binding?.tvExerciseCardio?.visibility = View.INVISIBLE
        binding?.flExerciseCardio?.visibility = View.INVISIBLE
        binding?.ivImageCardio?.visibility = View.INVISIBLE
        if (restTimer!=null){
            restTimer?.cancel()
            restProgress = 0

        }

        //get next exercise name
        binding?.tvUpcomingExerciseCardio?.text = exerciseList!![currentExercisePosition +1].getName()

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBarRest?.visibility = View.INVISIBLE
        binding?.tvTitleCardio?.visibility = View.INVISIBLE
        binding?.tvExerciseCardio?.visibility = View.VISIBLE
        binding?.flExerciseCardio?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseCardio?.visibility = View.INVISIBLE
        binding?.ivImageCardio?.visibility = View.VISIBLE
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding?.ivImageCardio?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseCardio?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        restProgress = 0 // Reset progress to 0 before starting the timer
        binding?.progressBarCardio?.progress = 0 // Set progress bar to 0 initially

        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBarCardio?.progress = restProgress // Update progress bar with the current progress
                binding?.tvTimerCardio?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {

        binding?.progressBarExerciseCardio?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExerciseCardio?.progress = 30 - exerciseProgress // Update progress bar with the current progress
                binding?.tvTimerExerciseCardio?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setupRestView()
                }else{
                    Toast.makeText(this@CardioExerciseView, "Congratulations! you have completed the workout", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CardioExerciseView, MainActivity::class.java)
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