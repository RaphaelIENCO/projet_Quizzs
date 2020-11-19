package com.example.projet_quizzs.gestionQuizz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_quizzs.R
import com.example.projet_quizzs.modelQuizz.Question
import kotlinx.android.synthetic.main.activity_main.*

class AddQuestionActivity : AppCompatActivity()  {
    private val CODE_ADDPROPOSITIONACTIVITY = 4
    val CLE_1 = "key1"

    var questionFinal = Question()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addquestion)
    }

    fun annuler(view: View) {finish()}
    fun creerQuestion(view: View) {finish()}

    fun addPropostition(view: View) {
        val intent = Intent(this@AddQuestionActivity, AddPropositionActivity::class.java)
        startActivityForResult(intent, CODE_ADDPROPOSITIONACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_ADDPROPOSITIONACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val proposition = data?.extras!!.getString("key_1")
                val juste =  data?.extras!!.getBoolean("key_2")
                println("=============")
                println(proposition)
                println("=============")
                if(!proposition.equals("")){
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
            else -> {
            }
        }
    }

    override fun finish() {
        val data = Intent()
        questionFinal.setIntitule(findViewById<TextView>(R.id.add_question).text.toString())

        data.putExtra("key_1", questionFinal)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }

}
