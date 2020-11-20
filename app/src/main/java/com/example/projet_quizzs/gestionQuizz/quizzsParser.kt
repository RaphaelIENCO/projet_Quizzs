package com.example.projet_quizzs.gestionQuizz

import com.example.projet_quizzs.modelQuizz.Question
import com.example.projet_quizzs.modelQuizz.Quizz
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class quizzsParser {
    private var quizzs = ArrayList<Quizz>()
    private var quizz: Quizz? = null
    private var question: Question? = null
    private var text: String? = null

    fun parse(inputStream: InputStream): ArrayList<Quizz> {
        try {
            // Cree un Parser qui va parcourir chaque noeud
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            //parser sur le stream donné
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var isQuestion = false // Pour recup la question n'etant pas dans un noeud
            // debut parcours
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name // name du noeud courant
                when (eventType) {

                    XmlPullParser.START_TAG -> if (tagname.equals("Quizz", ignoreCase = false)) {//Si debut noeud Quizz --> cree un nouveau quizz
                        quizz = Quizz()
                        quizz!!.setType(parser.getAttributeValue(0))
                    }else if(tagname.equals("Question", ignoreCase = false)){//Si debut noeud Question --> cree une nouvelle question
                        question = Question()
                        isQuestion = true
                    }else if(tagname.equals("Proposition", ignoreCase = false)){

                    }else if(tagname.equals("Reponse", ignoreCase = false)){//Si noeud reponse --> set la reponse à la question courante
                        question?.setReponse(parser.getAttributeValue(0).toInt())
                    }
                    XmlPullParser.TEXT -> if(isQuestion){ // Si on est dans le debut du noeud Question on recup la question
                        text = parser.text
                        question?.setIntitule(text?.trim())
                        isQuestion = false
                    }else{
                        text = parser.text
                    }
                    XmlPullParser.END_TAG -> if (tagname.equals("Quizz", ignoreCase = false)) { // Ajoute le nouveau Quizz fini
                        quizz?.let { quizzs.add(it) }
                    } else if (tagname.equals("Question", ignoreCase = false)) {// Ajoute la question au Quizz courant
                        //question?.setIntitule(text?.trim())
                        quizz?.addQuestion(question)
                    }else if(tagname.equals("Proposition", ignoreCase = false)){ // Ajoute la proposition à la Question courante
                        question?.addProposition(text?.trim())
                    }

                    else -> {
                    }
                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return quizzs
    }
}