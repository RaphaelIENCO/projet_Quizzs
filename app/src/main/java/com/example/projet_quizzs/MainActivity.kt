package com.example.projet_quizzs

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val CODE_GESTIONACTIVITY = 1
    //val sharedQuizzs: SharedPreferences = getSharedPreferences("projet_Quizzs-quizzs",0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startGestion(view: View) {
        val intent = Intent(this@MainActivity, GestionActivity::class.java)
        startActivityForResult(intent,CODE_GESTIONACTIVITY)
    }
}