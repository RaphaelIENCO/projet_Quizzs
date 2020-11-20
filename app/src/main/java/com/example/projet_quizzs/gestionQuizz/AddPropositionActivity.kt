package com.example.projet_quizzs.gestionQuizz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_quizzs.R

class AddPropositionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addproposition)
    }

    fun creerProposition(view: View) {
        finish()
    }

    fun annuler(view: View) {
        findViewById<EditText>(R.id.add_proposition).setText("")
        finish()
    }

    override fun finish() {
        val data = Intent()
        val proposition = findViewById<EditText>(R.id.add_proposition).text.toString()
        val juste = findViewById<CheckBox>(R.id.add_prop_checkbox).isChecked
        data.putExtra("key_1", proposition)
        data.putExtra("key_2",juste)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }

}
