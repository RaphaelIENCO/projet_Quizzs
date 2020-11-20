package com.example.projet_quizzs.gestionQuizz



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_quizzs.modelQuizz.Quizz
import com.example.projet_quizzs.R
import com.example.projet_quizzs.mainQuizz.MainActivity


class GestionQuizzAdapter(contxt : Context, quizzL : ArrayList<Quizz>): RecyclerView.Adapter<GestionQuizzAdapter.ViewHolder>() {

    var ctx = contxt
    var quizzs = quizzL


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textItemQuizz: TextView
        var buttonEdit : Button

        init {
            textItemQuizz = itemView.findViewById(R.id.text_item_quizz)
            buttonEdit = itemView.findViewById(R.id.button_edit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.getContext())
        val v: View = inflater.inflate(R.layout.item_quizz_gestion, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return quizzs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name: String = quizzs.get(position).getType()
        holder.textItemQuizz.setText(name)
        holder.textItemQuizz.setOnClickListener { Toast.makeText(ctx, quizzs.get(position).getType(), Toast.LENGTH_LONG).show() }
        holder.buttonEdit.setOnClickListener{
            if (ctx is GestionActivity) {
                (ctx as GestionActivity).editQuizz(position)
            }
        }
    }
}
