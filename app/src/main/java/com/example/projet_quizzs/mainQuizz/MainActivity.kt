package com.example.projet_quizzs.mainQuizz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_quizzs.R
import com.example.projet_quizzs.gestionQuizz.GestionActivity
import com.example.projet_quizzs.gestionQuizz.quizzsParser
import com.example.projet_quizzs.modelQuizz.Question
import com.example.projet_quizzs.modelQuizz.Quizz
import com.example.projet_quizzs.modelQuizz.QuizzList
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

    var quizzs: QuizzList = QuizzList(); // objet contenant la liste de tous les quizzs
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var adapter : RecyclerView.Adapter<MainQuizzAdapter.ViewHolder>
    lateinit var vueQuizzList : RecyclerView
    private var point = 0

    private var currentQuizz = 0
    private var currentQuestion = 0

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

        var ctx = this

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

            println(arrayQuizzs.size)

            quizzs.addQuizzs(arrayQuizzs)

            //Rempli les sharedPreference avec le QuizzList
            val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
            val prefsEditor = mPrefs.edit()
            //L'objet est transformé en json
            val gson = Gson()
            val json = gson.toJson(quizzs)
            println(json)
            prefsEditor.putString("quizzs", json)
            prefsEditor.apply()

            //Scope pour run dans le thread qui permet de travailler dans le layout
            runOnUiThread {
                vueQuizzList = findViewById(R.id.vue_quizzs_main)
                layoutManager = LinearLayoutManager(ctx)
                vueQuizzList.setLayoutManager(layoutManager)
                adapter = MainQuizzAdapter(ctx, quizzs.getQuizzs())
                vueQuizzList.setAdapter(adapter)
            }
        }



    }

    // Lance la GestionActivity pour travailler sur les quizzs
    fun startGestion(view: View) {
        val intent = Intent(this@MainActivity, GestionActivity::class.java)
        startActivityForResult(intent,CODE_GESTIONACTIVITY)
    }

    // Gere le retour de la GestionActivity pour update la liste de quizz
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_GESTIONACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                updateListeQuizz()
            }
            else -> {
            }
        }
    }

    // Recupere la liste de quizz en SharedPreference et maj l'affichage
    fun updateListeQuizz(){
        val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
        val gson = Gson()
        val json = mPrefs.getString("quizzs", "")
        quizzs = gson.fromJson(json, QuizzList::class.java)
        println(quizzs.getSize())

        vueQuizzList = findViewById(R.id.vue_quizzs_main)
        layoutManager = LinearLayoutManager(this)
        vueQuizzList.setLayoutManager(layoutManager)
        adapter = MainQuizzAdapter(this, quizzs.getQuizzs())
        vueQuizzList.setAdapter(adapter)
    }

    // Debut d'un quizz
    fun startQuiz(id : Int) {
        point = 0 // tout remis à 0 pour être sur...
        currentQuestion = 0

        val start = findViewById<LinearLayout>(R.id.layoutStart)
        val game = findViewById<LinearLayout>(R.id.layoutGame)

        start.visibility = View.INVISIBLE
        game.visibility = View.VISIBLE // affiche l'interface de jeu

        currentQuestion = 0
        currentQuizz = id

        setUpQuestion(currentQuestion)
    }

    //Mets en place la question numéro idQuestion du Quizz courant
    fun setUpQuestion(idQuestion: Int){

        if(idQuestion >= quizzs.getQuizzs().get(currentQuizz).getNbrQuestion()){
            endQuiz(); // si plus de question --> phase de fin de jeu
            return
        }

        //MChangement de la question affichée
        val question = quizzs.getQuizzs().get(currentQuizz).getQuestion(idQuestion)
        val q = findViewById<TextView>(R.id.question)
        q.setText(question.getIntitule())

        //Vide les boutons precedents
        val propositionLayout = findViewById<LinearLayout>(R.id.proposition_jeu)
        if(propositionLayout.childCount != 0 ){
            propositionLayout.removeAllViews()
        }

        // cree un bouton pour chaque proposition et le mets dans le LinearLayout
        for (i in 0 until question.getProposition().size) {
            val buttonRep = Button(this)
            buttonRep.setText(question.getProposition().get(i))
            if(question.getReponseId()-1 == i){
                buttonRep.setOnClickListener {
                    repJuste(currentQuestion)
                }
            }else{
                buttonRep.setOnClickListener {
                    repFaux(currentQuestion)
                }
            }
            propositionLayout.addView(buttonRep)
        }
    }

    // affiche le score et propose le retour menu ou rejouer
    private fun endQuiz() {
        val endGameLayout = findViewById<LinearLayout>(R.id.layoutEndGame)
        val affichage = findViewById<TextView>(R.id.score_affiche)
        val game = findViewById<LinearLayout>(R.id.layoutGame)

        affichage.setText("Votre score est : $point")

        point = 0
        currentQuestion = 0

        // Cree le bouton pour rejouer le currentQuizz
        val buttonRep = Button(this)
        buttonRep.setText("Rejouer !")
        buttonRep.setOnClickListener {
            endGameLayout.removeViewAt(2)
            endGameLayout.visibility = View.INVISIBLE
            startQuiz(currentQuizz)
        }

        endGameLayout.addView(buttonRep)

        game.visibility = View.INVISIBLE
        endGameLayout.visibility = View.VISIBLE // affiche l'interface de fin de jeu
    }

    // Retour au menu
    fun backMenu(view : View) {
        val start = findViewById<LinearLayout>(R.id.layoutStart)
        val endGameLayout = findViewById<LinearLayout>(R.id.layoutEndGame)
        endGameLayout.removeViewAt(2)
        currentQuizz = 0

        endGameLayout.visibility = View.INVISIBLE
        start.visibility = View.VISIBLE // affiche l'interface du menu

    }

    // Si reponse fausse
    fun repFaux(idQ : Int) {
        Toast.makeText(this, "Faux", Toast.LENGTH_LONG).show()
        currentQuestion++
        setUpQuestion(currentQuestion)
    }

    // Si reponse juste (augmente les points)
    fun repJuste(idQ : Int) {
        Toast.makeText(this, "Juste", Toast.LENGTH_LONG).show()
        point++;
        currentQuestion++
        setUpQuestion(currentQuestion)
    }



}