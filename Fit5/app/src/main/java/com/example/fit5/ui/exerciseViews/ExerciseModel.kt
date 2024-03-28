package com.example.fit5.ui.exerciseViews


    class ExerciseModel (
        private var id: Int,
        private var name: String,
        private var image: Int,
        private var isCompleted: Boolean,
        private var isSelected: Boolean,

        ){
        fun getId() :Int {
            return id
        }
        fun setId(id: Int){
            this.id = id
        }

        fun getName(): String {
            return name
        }

        fun setName(name: String) {
            this.name = name
        }

        fun getImage(): Int {
            return image
        }

        fun setImage(image: Int) {
            this.image = image
        }

        fun isCompleted(): Boolean {
            return isCompleted
        }

        fun setCompleted(completed: Boolean) {
            isCompleted = completed
        }

        fun isSelected(): Boolean {
            return isSelected
        }

        fun setSelected(selected: Boolean) {
            isSelected = selected
        }

    }
