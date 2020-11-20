package com.example.projet_quizzs.modelQuizz

import com.example.projet_quizzs.modelQuizz.Quizz

// Objet pour stocker une liste de Quizz --> plus facile à stocker dans les SharedPreferences
class QuizzList {
    private var quizzs: ArrayList<Quizz> = ArrayList<Quizz>();

    fun addQuizzs(newQuizzs : ArrayList<Quizz>){
        quizzs = newQuizzs
    }

    fun addQuizz(quizz : Quizz){
        quizzs.add(quizz)
    }

    fun getSize(): Int {
        return quizzs.size
    }

    fun getQuizzs(): ArrayList<Quizz> {
        return quizzs
    }
}