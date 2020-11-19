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

class AddQuizzActivity : AppCompatActivity() {
    private val CODE_ADDQUESTIONACTIVITY = 3

    var quizzFinal = Quizz()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addquizz)
    }

    fun creerQuizz(view: View) {finish()}
    fun annuler(view: View) {finish()}

    fun addQuestion(view: View) {
        val intent = Intent(this@AddQuizzActivity, AddQuestionActivity::class.java)
        startActivityForResult(intent, CODE_ADDQUESTIONACTIVITY)
    }


    override fun finish() {
        val data = Intent()
        quizzFinal.setType(findViewById<TextView>(R.id.add_type).text.toString())

        data.putExtra("key_1", quizzFinal)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }


}
