package com.example.projet_quizzs

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
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var isQuestion = false
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("Quizz", ignoreCase = false)) {
                        quizz = Quizz()
                        quizz!!.setType(parser.getAttributeValue(0))
                    }else if(tagname.equals("Question", ignoreCase = false)){
                        question = Question()
                        //question?.setIntitule(parser.text?.trim())
                        isQuestion = true
                    }else if(tagname.equals("Proposition", ignoreCase = false)){

                    }else if(tagname.equals("Reponse", ignoreCase = false)){
                        question?.setReponse(parser.getAttributeValue(0).toInt())
                    }
                    XmlPullParser.TEXT -> if(isQuestion){
                        text = parser.text
                        question?.setIntitule(text?.trim())
                        isQuestion = false
                    }else{
                        text = parser.text
                    }
                    XmlPullParser.END_TAG -> if (tagname.equals("Quizz", ignoreCase = false)) {
                        quizz?.let { quizzs.add(it) }
                    } else if (tagname.equals("Question", ignoreCase = false)) {
                        //question?.setIntitule(text?.trim())
                        quizz?.addQuestion(question)
                    }else if(tagname.equals("Proposition", ignoreCase = false)){
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