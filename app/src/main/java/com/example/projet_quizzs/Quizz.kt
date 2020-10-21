package com.example.projet_quizzs

class Quizz {

    private var questions:ArrayList<Question> = ArrayList<Question>()

    fun addQuestion(question: Question?) {
        questions.add(question!!)
    }
}