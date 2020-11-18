package com.example.projet_quizzs

class Quizz {

    private var questions:ArrayList<Question> = ArrayList<Question>()
    private var type:String = ""

    fun addQuestion(question: Question?) {
        questions.add(question!!)
    }

    fun setType(t : String){
        type = t
    }
}