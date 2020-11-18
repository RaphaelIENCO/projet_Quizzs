package com.example.projet_quizzs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    private val CODE_GESTIONACTIVITY = 1

    var quizzs: QuizzList = QuizzList();

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
    }

    fun startGestion(view: View) {
        val intent = Intent(this@MainActivity, GestionActivity::class.java)
        startActivityForResult(intent,CODE_GESTIONACTIVITY)
    }

    fun checkSP(view: View) {
        val mPrefs = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
        val gson = Gson()
        val json = mPrefs.getString("quizzs", "")
        println("aled : ")
        println(json)
        quizzs = gson.fromJson(json,QuizzList::class.java)
        println(quizzs.getSize())
    }
}