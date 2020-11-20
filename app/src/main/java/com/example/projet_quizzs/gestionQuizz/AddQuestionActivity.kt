package com.example.projet_quizzs.gestionQuizz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_quizzs.R
import com.example.projet_quizzs.modelQuizz.Question
import com.example.projet_quizzs.modelQuizz.Quizz
import kotlinx.android.synthetic.main.activity_main.*

class AddQuestionActivity : AppCompatActivity()  {
    private val CODE_ADDPROPOSITIONACTIVITY = 2
    private val CODE_EDITPROPOSITIONACTIVITY = 3
    private var currentRequestCode = 0
    private var currentQuestionId = 0
    private var isAnnule = false

    var questionFinal = Question()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var data = intent
        currentRequestCode = data.extras?.getInt("requestCode")!!

        setContentView(R.layout.activity_addquestion)

        if(currentRequestCode == 3 ){
            currentQuestionId = data.extras?.getInt("idQuestion")!!
            questionFinal = data.extras?.getSerializable("questionToEdit") as Question
            findViewById<TextView>(R.id.add_question).setText(questionFinal.getIntitule())
            // Ajout liste question et ajout d'un listner dessus
            setUpListeProposition()
        }
    }

    fun setUpListeProposition(){
        val propositionLayout = findViewById<LinearLayout>(R.id.add_listProposition)
        if(propositionLayout.childCount > 0){
            propositionLayout.removeAllViews()
        }
        for (i in 0 until questionFinal.getProposition().size){
            val propositionToAdd = questionFinal.getProposition().get(i)
            val tv = TextView(this)

            println(propositionToAdd)

            tv.setOnClickListener {
                println("=============")
                println(i.toString())
                println("=============")
                val intent = Intent(this@AddQuestionActivity, AddPropositionActivity::class.java)
                intent.putExtra("requestCode",CODE_EDITPROPOSITIONACTIVITY)
                intent.putExtra("propositionToEdit",propositionToAdd)
                intent.putExtra("idProposition",i)
                startActivityForResult(intent, CODE_EDITPROPOSITIONACTIVITY)
            }
            tv.setText(propositionToAdd)

            propositionLayout.addView(tv)
        }
    }

    fun annuler(view: View) {
        findViewById<TextView>(R.id.add_question).setText("")
        isAnnule = true
        finish()
    }
    fun creerQuestion(view: View) {finish()}

    fun addPropostition(view: View) {
        val intent = Intent(this@AddQuestionActivity, AddPropositionActivity::class.java)
        intent.putExtra("requestCode",CODE_ADDPROPOSITIONACTIVITY)
        startActivityForResult(intent, CODE_ADDPROPOSITIONACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_ADDPROPOSITIONACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val proposition = data?.extras!!.getString("key_1")
                val juste =  data.extras!!.getBoolean("key_2")
                val isAnnuleProp = data.extras!!.getBoolean("annule")
                println("=============")
                println(proposition)
                println("=============")
                if(!proposition.equals("") && !isAnnuleProp){
                    val tv = TextView(this)
                    val propositionLayout = findViewById<LinearLayout>(R.id.add_listProposition)
                    tv.setText(proposition)

                    propositionLayout.addView(tv)
                    if(juste){
                        questionFinal.setReponse(questionFinal.getProposition().size+1)
                    }
                    questionFinal.addProposition(proposition)
                }
            }
            CODE_EDITPROPOSITIONACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val proposition = data?.extras!!.getString("key_1").toString()
                val juste =  data.extras!!.getBoolean("key_2")
                val propositionId = data.extras!!.getInt("idProposition")
                val isAnnuleProp = data.extras!!.getBoolean("annule")
                println("=============")
                println(propositionId.toString())
                println("=============")
                if(!isAnnuleProp) {
                    if (!proposition.equals("")) {
                        if (juste) {
                            questionFinal.setReponse(propositionId + 1)
                        }
                        questionFinal.setPropositionAt(propositionId, proposition)
                    } else {
                        questionFinal.removePropositionAt(propositionId)
                    }
                }
            }
            else -> {
            }
        }
        setUpListeProposition()
    }

    override fun finish() {
        val data = Intent()
        questionFinal.setIntitule(findViewById<TextView>(R.id.add_question).text.toString())

        if(currentRequestCode == 3){
            data.putExtra("idQuestion", currentQuestionId)
        }

        data.putExtra("key_1", questionFinal)
        data.putExtra("annule",isAnnule)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }

}
