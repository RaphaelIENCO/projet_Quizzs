package com.example.projet_quizzs

class Question {
    private var intitule:String? = null
    private var reponse:Int = 0
    private var propositions:ArrayList<String> = ArrayList<String>()

    fun addProposition(propos : String?){
        propositions.add(propos!!)
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

}