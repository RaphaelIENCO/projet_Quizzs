package com.example.projet_quizzs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.io.File

class GestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion)

        var quizzs = ArrayList<Quizz>()


    }

    fun updateQuizzs(view: View) {
        var xml= ""
        val task = asyncReadXML()
        GlobalScope.launch{
            task?.execute()
        }

        GlobalScope.launch{
            xml = task?.get()
            println("PRINT 1  " + xml)

            val file = File("Quizzs.xml")
            file.writeText(xml);


            /*val parser = quizzsParser()
            val istream = assets.open("src/main/res/values/Quizzs.xml")
            quizzs = parser.parse(istream)

            println(quizzs)*/

        }
        //println("PRINT 2  " + xml)

    }

}
