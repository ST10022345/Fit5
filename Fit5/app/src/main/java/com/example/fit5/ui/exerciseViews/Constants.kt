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
}