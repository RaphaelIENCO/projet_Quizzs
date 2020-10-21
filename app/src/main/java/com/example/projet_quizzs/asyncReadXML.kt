package com.example.projet_quizzs

import android.os.AsyncTask
import java.net.URL
import java.nio.charset.Charset

class asyncReadXML() : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String? {
        return URL("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml").readText(
            Charset.forName("ISO-8859-1"))
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}