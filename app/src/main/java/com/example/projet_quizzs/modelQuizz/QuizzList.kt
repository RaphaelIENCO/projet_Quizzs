package com.example.projet_quizzs.modelQuizz

import com.example.projet_quizzs.modelQuizz.Quizz

class QuizzList {
    private var quizzs: ArrayList<Quizz> = ArrayList<Quizz>();

    fun addQuizzs(newQuizzs : ArrayList<Quizz>){
        quizzs = newQuizzs
    }

    fun getSize(): Int {
        return quizzs.size
    }

    fun getQuizzs(): ArrayList<Quizz> {
        return quizzs
    }
}