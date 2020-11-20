package com.example.projet_quizzs.modelQuizz

import com.example.projet_quizzs.modelQuizz.Question
import java.io.Serializable

// Objet representant un Quizz (liste de Question et le type)
// Serializable pour pouvoir passer l'objet entre Activity
class Quizz : Serializable {

    private var questions:ArrayList<Question> = ArrayList<Question>()
    private var type:String = ""

    fun addQuestion(question: Question?) {
        questions.add(question!!)
    }

    fun setType(t : String){
        type = t
    }

    fun getType() : String{
        return type
    }

    fun setQuestionAt(id : Int, q : Question) {
        questions.set(id,q)
    }

    fun removeQuestionAt(id : Int) {
        questions.removeAt(id)
    }

    fun getQuestion(id : Int) : Question{
        return questions.get(id)
    }

    fun getNbrQuestion() : Int {
        return questions.size
    }
}