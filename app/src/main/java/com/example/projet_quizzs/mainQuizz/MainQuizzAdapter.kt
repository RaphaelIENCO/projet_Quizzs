package com.example.projet_quizzs.mainQuizz




import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_quizzs.R
import com.example.projet_quizzs.modelQuizz.Quizz

// Class qui permet de remplir le RecyclerVIew dans le MainActivity pour chaque item de la liste de quizz
class MainQuizzAdapter(contxt : Context, quizzL : ArrayList<Quizz>): RecyclerView.Adapter<MainQuizzAdapter.ViewHolder>() {

    var ctx = contxt
    var quizzs = quizzL

    // Class du ViewHolder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textItemQuizz: TextView
        var buttonPlay : Button
        // Chaque item contient un TextView et un Button à modifier en fonction de l'item

        init {
            textItemQuizz = itemView.findViewById(R.id.text_item_quizz_main)
            buttonPlay = itemView.findViewById(R.id.button_jouer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.getContext())
        val v: View = inflater.inflate(R.layout.item_quizz_main, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return quizzs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name: String = quizzs.get(position).getType()
        holder.textItemQuizz.setText(name)
        holder.textItemQuizz.setOnClickListener { Toast.makeText(ctx, quizzs.get(position).getType(), Toast.LENGTH_LONG).show() }
        holder.buttonPlay.setOnClickListener{
            if (ctx is MainActivity) {
                //Quand on clique sur le bouton Jouer : lance la fonction startQuiz qui lance la phase de jeu
                (ctx as MainActivity).startQuiz(position)
            }
        }
    }
}
