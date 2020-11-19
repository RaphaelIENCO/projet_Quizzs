package com.example.projet_quizzs.gestionQuizz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_quizzs.R
import com.example.projet_quizzs.modelQuizz.Question
import com.example.projet_quizzs.modelQuizz.Quizz
import com.example.projet_quizzs.modelQuizz.QuizzList
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class GestionActivity : AppCompatActivity() {
    private val CODE_ADDACTIVITY = 2

    var quizzs: QuizzList = QuizzList();
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var adapter : RecyclerView.Adapter<GestionQuizzAdapter.ViewHolder>
    lateinit var vueQuizzList : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion)
        //var quizzs = ArrayList<Quizz>()

        val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
        val gson = Gson()
        val json = mPrefs.getString("quizzs", "")
       /* println("aled : ")
        println(json)
        println("==========")
        println(gson.fromJson(json, QuizzList::class.java))*/
        quizzs = gson.fromJson(json, QuizzList::class.java)
        println(quizzs.getSize())

        vueQuizzList = findViewById(R.id.vue_quizzs_gestion)
        layoutManager = LinearLayoutManager(this)
        vueQuizzList.setLayoutManager(layoutManager)
        adapter = GestionQuizzAdapter(this, quizzs.getQuizzs())
        vueQuizzList.setAdapter(adapter)

    }

    fun updateQuizzs(view: View) {
        var xml= ""

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



        /*GlobalScope.launch{
            task?.execute()
        }

        GlobalScope.launch{
            xml = task?.get()
            //println("PRINT 1  " + xml)

            val factory = DocumentBuilderFactory.newInstance();
            val builder = factory.newDocumentBuilder();
            val inputSource = InputSource(StringReader(xml));
            val xmlDOM = builder.parse(inputSource);

            //println(builder.parse(inputSource));


            *//*val file = File("Quizzs.xml")
            file.writeText(xml);*//*
            val xmlSource = DOMSource(xmlDOM);
            val outputStream = ByteArrayOutputStream()
            val outputTarget = StreamResult(outputStream)
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget)
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())

            val parser = quizzsParser()
            //val istream = assets.open("src/main/res/values/Quizzs.xml")
            quizzs = parser.parse(inputStream)

            println(quizzs.size)

        }*/

    }

    fun addQuizz(view: View) {
        val intent = Intent(this@GestionActivity, AddQuizzActivity::class.java)
        startActivityForResult(intent, CODE_ADDACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_ADDACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val quizzToAdd = data?.getSerializableExtra("key_1") as Quizz
                println("=============")
                println(quizzToAdd.getType())
                println("=============")

                if(!quizzToAdd.getType().equals("") && quizzToAdd.getNbrQuestion() >=1){
                    quizzs.addQuizz(quizzToAdd)

                    val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
                    val prefsEditor = mPrefs.edit()
                    val gson = Gson()
                    val json = gson.toJson(quizzs)
                    prefsEditor.putString("quizzs", json)
                    prefsEditor.apply()
                }

            }
            else -> {
            }
        }
    }

}
