package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtikelAdapter (private val artikels: MutableList<Artikel>) : RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder>() {

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addArtikel(artikel: Artikel){
        artikels.add(artikel)
        notifyItemInserted(artikels.size-1)
    }

    private fun toggleStrikeThrough(tvArtikelTitle: TextView, isChecked: Boolean){
        if(isChecked){
            tvArtikelTitle.paintFlags = tvArtikelTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvArtikelTitle.paintFlags = tvArtikelTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        return ArtikelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_artikel,parent,false))
    }

    override fun getItemCount(): Int {
        return artikels.size
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val currentArtikel = artikels[position];
        holder.itemView.apply {
            var artikelTitle = findViewById<TextView>(R.id.tvArtikelTitle)
            var checkBox = findViewById<CheckBox>(R.id.cbArtikelDone)
            artikelTitle.text = currentArtikel.title
            checkBox.isChecked = currentArtikel.isChecked
            toggleStrikeThrough(artikelTitle, checkBox.isChecked)
            checkBox.setOnCheckedChangeListener { _, isChecked -> toggleStrikeThrough(artikelTitle, isChecked) }
            currentArtikel.isChecked = !currentArtikel.isChecked
        }
    }
}