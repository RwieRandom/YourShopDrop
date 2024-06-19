package de.your.yourshopdrop

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter (private val listItemManager: ListItemManager) : RecyclerView.Adapter<ListAdapter.ArtikelViewHolder>() {

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun add(item: ListItem){
        listItemManager.addItem(item)
        notifyItemInserted(itemCount -1)
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
        return listItemManager.getItemCount()
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val currentItem = listItemManager.getItem(position);

        holder.itemView.apply {
            val itemTitle = findViewById<TextView>(R.id.tvArtikelTitle)
            val checkBox = findViewById<CheckBox>(R.id.cbArtikelDone)

            itemTitle.text = currentItem.title
            checkBox.isChecked = currentItem.isChecked
            toggleStrikeThrough(itemTitle, checkBox.isChecked)
            /*checkBox.setOnCheckedChangeListener {_, isChecked ->
                if(isChecked){
                    listItemManager.removeItem(currentItem)
                    notifyItemRemoved(listItemManager.getItemPosition(currentItem))
                }

            }
            currentItem.isChecked = !currentItem.isChecked*/
        }
    }
}