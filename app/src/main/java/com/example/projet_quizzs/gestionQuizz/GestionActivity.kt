package com.example.projet_quizzs.gestionQuizz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
    private val CODE_EDITACTIVITY = 3

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
        quizzs = gson.fromJson(json, QuizzList::class.java)
        println(quizzs.getSize())

        //Rempli le Recycler view
        vueQuizzList = findViewById(R.id.vue_quizzs_gestion)
        layoutManager = LinearLayoutManager(this)
        vueQuizzList.setLayoutManager(layoutManager)
        adapter = GestionQuizzAdapter(this, quizzs.getQuizzs())
        vueQuizzList.setAdapter(adapter)

    }

    fun updateQuizzs(view: View) {
        var xml= ""
        val ctx = this

        //Coroutine pour ne pas bloquer le thread principal
        GlobalScope.launch{
            //Recupere le contenu du DOM de l'url
            val webBuilderFactory = DocumentBuilderFactory.newInstance()
            val webDocBuilder = webBuilderFactory.newDocumentBuilder()
            val xmlDOM = webDocBuilder.parse(InputSource("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml"))
            val xmlSource = DOMSource(xmlDOM);

            //Transform le document DOM (xml) en InputStream pour le Parser
            val outputStream = ByteArrayOutputStream()
            val outputTarget = StreamResult(outputStream)
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget)
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())

            //Parser pour le InputStream cree
            val parser = quizzsParser()
            val arrayQuizzs = parser.parse(inputStream)


            quizzs.addQuizzs(arrayQuizzs)

            //Rempli les sharedPreference avec le QuizzList
            val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
            val prefsEditor = mPrefs.edit()
            //L'objet QuizzList est transformé en json
            val gson = Gson()
            val json = gson.toJson(quizzs)
            prefsEditor.putString("quizzs", json)
            prefsEditor.apply()

            //Scope pour run dans le thread qui permet de travailler dans le layout
            runOnUiThread {
                vueQuizzList = findViewById(R.id.vue_quizzs_gestion)
                layoutManager = LinearLayoutManager(ctx)
                vueQuizzList.setLayoutManager(layoutManager)
                adapter = GestionQuizzAdapter(ctx, quizzs.getQuizzs())
                vueQuizzList.setAdapter(adapter)
            }
        }

    }

    //Lance une AddQuizzActivity en mode d'ajout
    fun addQuizz(view: View) {
        val intent = Intent(this@GestionActivity, AddQuizzActivity::class.java)
        intent.putExtra("requestCode",CODE_ADDACTIVITY)
        startActivityForResult(intent, CODE_ADDACTIVITY)
    }

    //Lance une AddQuizzActivity en mode d'edit
    fun editQuizz(id : Int){
        val intent = Intent(this@GestionActivity, AddQuizzActivity::class.java)
        intent.putExtra("requestCode",CODE_EDITACTIVITY)
        intent.putExtra("quizzToEdit",quizzs.getQuizzs().get(id))
        intent.putExtra("idQuizz",id)
        startActivityForResult(intent, CODE_EDITACTIVITY)
    }

    //Fonction pour gerer le retour de l'activité AddQuizzActivity en fonction de son mode
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_ADDACTIVITY -> if (resultCode == Activity.RESULT_OK) { // Retour ajout
                val quizzToAdd = data?.getSerializableExtra("key_1") as Quizz
                val isAnnuleQzz = data.extras!!.getBoolean("annule")

                if(!quizzToAdd.getType().equals("") && quizzToAdd.getNbrQuestion() >=1 && !isAnnuleQzz){
                    quizzs.addQuizz(quizzToAdd) // ajoute si le type n'est pas, 1 question au moins et pas d'annulation

                    // maj les SharedPreference
                    val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
                    val prefsEditor = mPrefs.edit()
                    val gson = Gson()
                    val json = gson.toJson(quizzs)
                    prefsEditor.putString("quizzs", json)
                    prefsEditor.apply()
                }

            }
            CODE_EDITACTIVITY -> if (resultCode == Activity.RESULT_OK) { // Retour edit
                val quizzToEdit = data?.getSerializableExtra("key_1") as Quizz
                val idQuizzToEdit = data.getIntExtra("idQuizz",0)
                val isAnnuleQst = data.extras!!.getBoolean("annule")

                if(!isAnnuleQst) {
                    if (!quizzToEdit.getType().equals("") && quizzToEdit.getNbrQuestion() >= 1) {
                        quizzs.getQuizzs().set(idQuizzToEdit, quizzToEdit) // Update le quizz qui avait été selectionné

                    } else {
                        quizzs.getQuizzs().removeAt(idQuizzToEdit) // Si on a vide le type de quizz ou retiré les questions : on remove
                    }
                }

                // maj les SharedPreference
                val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
                val prefsEditor = mPrefs.edit()
                val gson = Gson()
                val json = gson.toJson(quizzs)
                prefsEditor.putString("quizzs", json)
                prefsEditor.apply()
            }
            else -> {
            }
        }
        vueQuizzList = findViewById(R.id.vue_quizzs_gestion)
        layoutManager = LinearLayoutManager(this)
        vueQuizzList.setLayoutManager(layoutManager)
        adapter = GestionQuizzAdapter(this, quizzs.getQuizzs())
        vueQuizzList.setAdapter(adapter)
    }

    // pour que le retour soit geré dans la MainActivity
    override fun finish() {
        val data = Intent()
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }

}
