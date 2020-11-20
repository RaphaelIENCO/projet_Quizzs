package com.example.projet_quizzs.modelQuizz

import java.io.Serializable

class Question : Serializable{
    private var intitule:String? = null
    private var reponse:Int = 0
    private var propositions:ArrayList<String> = ArrayList<String>()

    fun addProposition(propos : String?){
        propositions.add(propos!!)
    }

    fun setPropositionAt(id : Int ,p : String){
        propositions.set(id,p)
    }

    fun removePropositionAt(id: Int) {
        propositions.removeAt(id)
    }

    fun setReponse( rep : Int?){
        reponse = rep!!
    }

    fun setIntitule( intit : String?){
        intitule = intit!!
    }

    fun getAnswer() : String{
        return propositions.get(reponse-1)
    }

    fun getReponseId() : Int{
        return reponse
    }

    fun getIntitule(): String? {
        return intitule
    }

    fun getProposition() : ArrayList<String> {
        return propositions
    }



}