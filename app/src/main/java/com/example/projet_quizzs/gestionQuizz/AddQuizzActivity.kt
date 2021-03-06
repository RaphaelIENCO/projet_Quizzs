package com.example.projet_quizzs.gestionQuizz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_quizzs.R
import com.example.projet_quizzs.modelQuizz.Question
import com.example.projet_quizzs.modelQuizz.Quizz
import com.google.gson.Gson

class AddQuizzActivity : AppCompatActivity() {
    private val CODE_ADDQUESTIONACTIVITY = 2
    private val CODE_EDITQUESTIONACTIVITY = 3
    private var currentRequestCode = 0
    private var currentQuizzId = 0
    private var isAnnule = false

    private var quizzFinal = Quizz()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var data = intent
        currentRequestCode = data.extras?.getInt("requestCode")!!


        setContentView(R.layout.activity_addquizz)

        // Si on est en mode edit --> affiche les questions
        if(currentRequestCode == 3 ){
            currentQuizzId = data.extras?.getInt("idQuizz")!!
            quizzFinal = data.extras?.getSerializable("quizzToEdit") as Quizz
            findViewById<TextView>(R.id.add_type).setText(quizzFinal.getType())
            // Ajout liste question et ajout d'un listner dessus
            setUpListeQuestion()
        }
    }

    // Pour chaque question cree un TextView
    fun setUpListeQuestion(){
        val questionLayout = findViewById<LinearLayout>(R.id.add_listQuestion)
        if(questionLayout.childCount > 0){
            questionLayout.removeAllViews()
        }
        for (i in 0 until quizzFinal.getNbrQuestion()){
            val questionToAdd = quizzFinal.getQuestion(i)
            val tv = TextView(this)

            // Listner pour Edit la question
            tv.setOnClickListener {
                val intent = Intent(this@AddQuizzActivity, AddQuestionActivity::class.java)
                intent.putExtra("requestCode",CODE_EDITQUESTIONACTIVITY)
                intent.putExtra("questionToEdit",questionToAdd)
                intent.putExtra("idQuestion",i)
                startActivityForResult(intent, CODE_EDITQUESTIONACTIVITY)
            }
            tv.setText(questionToAdd.getIntitule())


            questionLayout.addView(tv)
        }
    }


    fun creerQuizz(view: View) {finish()}

    fun annuler(view: View) {
        findViewById<TextView>(R.id.add_type).setText("")
        isAnnule = true //set pour ne pas supprimer le quizz
        finish()
    }

    //Lance une AddQuestionActivity en mode d'ajout pour ce Quizz
    fun addQuestion(view: View) {
        val intent = Intent(this@AddQuizzActivity, AddQuestionActivity::class.java)
        intent.putExtra("requestCode",CODE_ADDQUESTIONACTIVITY)
        startActivityForResult(intent, CODE_ADDQUESTIONACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_ADDQUESTIONACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val questionToAdd = data?.getSerializableExtra("key_1") as Question
                val isAnnuleQst = data.extras!!.getBoolean("annule")

                // ajoute la nouvelle question
                if(!questionToAdd.getIntitule().equals("") && questionToAdd.getProposition().size >=2 && !isAnnuleQst){
                    quizzFinal.addQuestion(questionToAdd)
                }

            }
            CODE_EDITQUESTIONACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val questionToEdit = data?.getSerializableExtra("key_1") as Question
                val idQuestionToEdit = data.getIntExtra("idQuestion",0)
                val isAnnuleQst = data.extras!!.getBoolean("annule")

                if(!isAnnuleQst) {
                    // Si correctement renseigné, ajoute la question
                    if (!questionToEdit.getIntitule().equals("") && questionToEdit.getProposition().size >= 2) {
                        quizzFinal.setQuestionAt(idQuestionToEdit, questionToEdit)
                    } else {
                        // Sinon la remove
                        quizzFinal.removeQuestionAt(idQuestionToEdit)
                    }
                }

            }
            else -> {
            }
        }
        setUpListeQuestion()
    }


    override fun finish() {
        val data = Intent()
        quizzFinal.setType(findViewById<TextView>(R.id.add_type).text.toString())

        // Renvoi l'id du Quizz modifié
        if(currentRequestCode == 3){
            data.putExtra("idQuizz", currentQuizzId)
        }

        data.putExtra("key_1", quizzFinal)
        data.putExtra("annule",isAnnule)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }


}
