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
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("Quizz", ignoreCase = false)) {
                        // create a new instance of quizz
                        quizz = Quizz()
                    }else if(tagname.equals("Question", ignoreCase = false)){
                        question = Question()
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> if (tagname.equals("Quizz", ignoreCase = false)) {
                        // add employee object to list
                        quizz?.let { quizzs.add(it) }
                    } else if (tagname.equals("Question", ignoreCase = false)) {
                        question?.setIntitule(text)
                        quizz?.addQuestion(question)
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