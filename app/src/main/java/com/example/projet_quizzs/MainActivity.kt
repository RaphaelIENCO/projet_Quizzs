package com.example.projet_quizzs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class MainActivity : AppCompatActivity() {
    private val CODE_GESTIONACTIVITY = 1

    var quizzs: QuizzList = QuizzList();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mPrefs = getSharedPreferences("PREF_NAME",Context.MODE_PRIVATE)
        val testQuizzs = ArrayList<Quizz>()
        testQuizzs.add(Quizz())
        testQuizzs.add(Quizz())
        println(testQuizzs.size)
        quizzs.addQuizzs(testQuizzs)

        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(quizzs)
        println(json)
        prefsEditor.putString("quizzs", json)
        prefsEditor.apply()
        var xml= ""
        val task = asyncReadXML()

        GlobalScope.launch{
            val webBuilderFactory = DocumentBuilderFactory.newInstance()
            val webDocBuilder = webBuilderFactory.newDocumentBuilder()
            val xmlDOM = webDocBuilder.parse(InputSource("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml"))
            val xmlSource = DOMSource(xmlDOM);
            val outputStream = ByteArrayOutputStream()
            val outputTarget = StreamResult(outputStream)
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget)
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())

            val parser = quizzsParser()
            //val istream = assets.open("src/main/res/values/Quizzs.xml")
            val arrayQuizzs = parser.parse(inputStream)

            println(arrayQuizzs.size)

            quizzs.addQuizzs(arrayQuizzs)

            val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
            val prefsEditor = mPrefs.edit()
            val gson = Gson()
            val json = gson.toJson(quizzs)
            println(json)
            prefsEditor.putString("quizzs", json)
            prefsEditor.apply()
        }
    }

    fun startGestion(view: View) {
        val intent = Intent(this@MainActivity, GestionActivity::class.java)
        startActivityForResult(intent,CODE_GESTIONACTIVITY)
    }

    fun checkSP(view: View) {
        val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
        val gson = Gson()
        val json = mPrefs.getString("quizzs", "")
        println("aled : ")
        println(json)
        quizzs = gson.fromJson(json,QuizzList::class.java)
        println(quizzs.getSize())
    }
}