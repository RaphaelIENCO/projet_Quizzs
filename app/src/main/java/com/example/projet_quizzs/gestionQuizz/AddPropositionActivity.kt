package com.example.projet_quizzs.gestionQuizz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_quizzs.R
import com.example.projet_quizzs.modelQuizz.Question

class AddPropositionActivity : AppCompatActivity() {
    private var currentRequestCode = 0
    private var currentPropositionId = 0
    private var isAnnule = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var data = intent
        currentRequestCode = data.extras?.getInt("requestCode")!!

        setContentView(R.layout.activity_addproposition)

        if(currentRequestCode == 3 ){
            currentPropositionId = data.extras?.getInt("idProposition")!!
            val propositiontoEdit = data.extras?.getString("propositionToEdit")
            println("==============")
            println(propositiontoEdit)
            println("==============")
            findViewById<TextView>(R.id.add_proposition).setText(propositiontoEdit)
        }
    }

    fun creerProposition(view: View) {
        finish()
    }

    fun annuler(view: View) {
        findViewById<EditText>(R.id.add_proposition).setText("")
        isAnnule = true
        finish()
    }

    override fun finish() {
        val data = Intent()
        val proposition = findViewById<EditText>(R.id.add_proposition).text.toString()
        val juste = findViewById<CheckBox>(R.id.add_prop_checkbox).isChecked
        if(currentRequestCode == 3){
            data.putExtra("idProposition", currentPropositionId)
        }
        data.putExtra("key_1", proposition)
        data.putExtra("key_2",juste)
        data.putExtra("annule",isAnnule)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }

}
