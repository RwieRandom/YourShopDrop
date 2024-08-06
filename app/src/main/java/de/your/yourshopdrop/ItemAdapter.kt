package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

//TODO: Werden items abgehakt, dann werden sie nach ganz unten verschoben. Wird ein neues item hinzugefügt, soll das über den abgehakten hinzugefügt werden
class ItemAdapter(private val itemManager: ItemManager) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var swipedPosition = -1
    private var renamePosition = -1

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun add(item: ListItem) {
        itemManager.addItem(item)
        notifyItemInserted(itemCount - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val holder = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_item_list, parent, false))
        return holder
    }

    override fun getItemCount(): Int {
        return itemManager.getItemCount()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemManager.getItem(position)

        holder.itemView.apply {
            val itemTitle = findViewById<TextView>(R.id.tvItemTitle)
            val itemQuantity = findViewById<TextView>(R.id.tvItemQuantity)
            val checkBox = findViewById<CheckBox>(R.id.cbItemChecked)
            val swipeLayout = findViewById<LinearLayout>(R.id.swipeLayout)
            val renameLayout = findViewById<TextInputLayout>(R.id.container_renameItem)
            val renameEditText = findViewById<TextInputEditText>(R.id.input_rename_item)
            val btnRename = findViewById<ImageButton>(R.id.btnSwipeRename)
            val btnDelete = findViewById<ImageButton>(R.id.btnSwipeDelete)

            itemTitle.text = currentItem.title
            itemQuantity.text = currentItem.quantity
            checkBox.isChecked = currentItem.isChecked
            setStrikethrough(itemTitle, currentItem.isChecked)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                itemManager.updateItem(currentItem, isChecked)
                setStrikethrough(itemTitle, isChecked)
            }

            swipeLayout.visibility = if (position == swipedPosition) View.VISIBLE else View.GONE

            if(renamePosition == position){
                renameLayout.visibility = View.VISIBLE
                renameLayout.hint = currentItem.title
                itemTitle.visibility = View.GONE
                checkBox.visibility = View.GONE
            } else {
                renameLayout.visibility = View.GONE
                itemTitle.visibility = View.VISIBLE
                checkBox.visibility = View.VISIBLE
            }

            btnRename.setOnClickListener {
                showRenameLayout(position)
            }

            btnDelete.setOnClickListener {
                deleteItem(position)
                swipedPosition = -1
            }

            renameEditText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                    val newName = renameEditText.text.toString()
                    renameEditText.setText("")
                    itemManager.renameItem(position, newName)
                    renamePosition = -1
                    notifyItemChanged(position)

                    Tools.hideKeyboard(v)
                    true
                } else {
                    false
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteCheckedItems() {
        val itemsToDelete = mutableListOf<ListItem>()

        for (item in itemManager.loadItems()) {
            if (item.isChecked) {
                itemsToDelete.add(item)
            }
        }

        for (item in itemsToDelete) {
            itemManager.removeItem(item)
        }

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteList() {
        itemManager.deleteAllItems()
        notifyDataSetChanged()
    }

    private fun deleteItem(position: Int) {
        itemManager.removeItem(position)
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

    fun clearRenamePosition() {
        val previousRenamePosition = renamePosition
        renamePosition = -1
        notifyItemChanged(previousRenamePosition)
    }

    private fun setStrikethrough(textView: TextView, isChecked: Boolean) {
        if (isChecked) {
            textView.paintFlags = textView.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun getRenamePosition(): Int {
        return renamePosition
    }

    private fun showRenameLayout(position: Int) {
        renamePosition = position
        notifyItemChanged(position)
    }
}


