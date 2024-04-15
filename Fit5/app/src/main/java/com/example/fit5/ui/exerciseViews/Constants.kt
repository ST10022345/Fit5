package com.example.fit5.ui.exerciseViews

import com.example.fit5.R

object Constants {
    fun defaultExerciseListChest(): ArrayList<ExerciseModel>{
        val exerciseListChest = ArrayList<ExerciseModel>()
        val singleArmPushup = ExerciseModel(
            1,
            "single Arm Pushup",
            R.drawable.chest1,
            false,
            false
        )
        exerciseListChest.add(singleArmPushup)

        val DiamondPushup = ExerciseModel(
            2,
            "Diamond Pushup",
            R.drawable.chest2,
            false,
            false
        )
        exerciseListChest.add(DiamondPushup)

        val dumbellPressup = ExerciseModel(
            3,
            "Dumbell PressUp",
            R.drawable.chest3,
            false,
            false
        )
        exerciseListChest.add(dumbellPressup)

        val DumbellPress = ExerciseModel(
            4,
            "Dumbell Press",
            R.drawable.chest4,
            false,
            false
        )
        exerciseListChest.add(DumbellPress)

        val NormalPushup = ExerciseModel(
            5,
            "Normal Pushup",
            R.drawable.chest5,
            false,
            false
        )
        exerciseListChest.add(NormalPushup)

        return exerciseListChest
    }

    fun defaultExerciseListCardio(): ArrayList<ExerciseModel> {
        val exerciseListCardio = ArrayList<ExerciseModel>()

        val crunches = ExerciseModel(
            1,
            "Crunches",
            R.drawable.cardio1,
            false,
            false
        )
        exerciseListCardio.add(crunches)

        val pushupAndStarJump = ExerciseModel(
            2,
            "pushup and star jump",
            R.drawable.cardio2,
            false,
            false
        )
        exerciseListCardio.add(pushupAndStarJump)

        val mountainClimbers = ExerciseModel(
            3,
            "mountain climbers",
            R.drawable.cardio3,
            false,
            false
        )
        exerciseListCardio.add(mountainClimbers)

        val kettleSwing= ExerciseModel(
            4,
            "kettle bell swings",
            R.drawable.cardio4,
            false,
            false
        )
        exerciseListCardio.add(kettleSwing)

        val highKick = ExerciseModel(
            5,
            "higher kicks",
            R.drawable.cardio5,
            false,
            false
        )
        exerciseListCardio.add(highKick)

        return exerciseListCardio
    }

    fun defaultExerciseListBack(): ArrayList<ExerciseModel> {
        val exerciseListBack = ArrayList<ExerciseModel>()

        val HorizontalRow = ExerciseModel(
            1,
            "Horizontal Rows",
            R.drawable.back1,
            false,
            false
        )
        exerciseListBack.add(HorizontalRow)

        val backAndShoulderPress = ExerciseModel(
            2,
            "Back And Shoulder Press ",
            R.drawable.back2,
            false,
            false
        )
        exerciseListBack.add(backAndShoulderPress )

        val WidePullups = ExerciseModel(
            3,
            "WidePullups",
            R.drawable.back3,
            false,
            false
        )
        exerciseListBack.add(WidePullups)

        val BarBellPress = ExerciseModel(
            4,
            "Bar Bell Press",
            R.drawable.back4,
            false,
            false
        )
        exerciseListBack.add(BarBellPress)

        val arnoldPress = ExerciseModel(
            5,
            "Arnold Press",
            R.drawable.back5,
            false,
            false
        )
        exerciseListBack.add(arnoldPress)

        return exerciseListBack
    }
}