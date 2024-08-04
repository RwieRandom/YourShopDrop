package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val listItemManager: ListItemManager) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    private var swipedPosition = -1

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun add(item: ListItem) {
        listItemManager.addItem(item)
        notifyItemInserted(itemCount - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val holder = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_item_list, parent, false))
        return holder
    }

    override fun getItemCount(): Int {
        return listItemManager.getItemCount()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = listItemManager.getItem(position)

        holder.itemView.apply {
            val itemTitle = findViewById<TextView>(R.id.tvItemTitle)
            val checkBox = findViewById<CheckBox>(R.id.cbItemChecked)
            val swipeLayout = findViewById<LinearLayout>(R.id.swipeLayout)
            val btnRename = findViewById<ImageButton>(R.id.btnSwipeRename)
            val btnDelete = findViewById<ImageButton>(R.id.btnSwipeDelete)

            itemTitle.text = currentItem.title
            checkBox.isChecked = currentItem.isChecked
            setStrikethrough(itemTitle, currentItem.isChecked)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                listItemManager.updateItem(currentItem, isChecked)
                setStrikethrough(itemTitle, isChecked)
            }

            swipeLayout.visibility = if (position == swipedPosition) View.VISIBLE else View.GONE

            btnRename.setOnClickListener {
                // Handle rename logic here
                notifyItemChanged(swipedPosition)
                swipedPosition = -1
            }

            btnDelete.setOnClickListener {
                deleteItem(position)
                swipedPosition = -1
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteCheckedItems() {
        val itemsToDelete = mutableListOf<ListItem>()

        // Find checked items
        for (item in listItemManager.loadItems()) {
            if (item.isChecked) {
                itemsToDelete.add(item)
            }
        }

        // Remove checked items
        for (item in itemsToDelete) {
            listItemManager.removeItem(item)
        }

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteList() {
        listItemManager.deleteAllItems()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        listItemManager.removeItem(position)
        notifyItemRemoved(position)
    }

    fun setSwipedPosition(position: Int) {
        val previousSwipedPosition = swipedPosition
        swipedPosition = position
        notifyItemChanged(previousSwipedPosition)
        notifyItemChanged(swipedPosition)
    }

    fun clearSwipedPosition() {
        val previousSwipedPosition = swipedPosition
        swipedPosition = -1
        notifyItemChanged(previousSwipedPosition)
    }

    private fun setStrikethrough(textView: TextView, isChecked: Boolean) {
        if (isChecked) {
            textView.paintFlags = textView.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

