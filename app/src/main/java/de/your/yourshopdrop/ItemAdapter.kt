package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

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
            val itemUnit = findViewById<TextView>(R.id.tvItemUnit)
            val checkBox = findViewById<CheckBox>(R.id.cbItemChecked)
            val swipeLayout = findViewById<LinearLayout>(R.id.swipeLayout)
            val editItemLayout = findViewById<LinearLayout>(R.id.editItemContainer)
            val editItemTitle = findViewById<TextInputEditText>(R.id.editItemTitle)
            val editItemQuantity = findViewById<TextInputEditText>(R.id.editItemQuantity)
            val editItemUnit = findViewById<Spinner>(R.id.editItemUnit)
            val units = context.resources.getStringArray(R.array.quantity_units)
            val btnRename = findViewById<ImageButton>(R.id.btnSwipeRename)
            val btnDelete = findViewById<ImageButton>(R.id.btnSwipeDelete)

            itemTitle.text = currentItem.title
            itemQuantity.text = currentItem.quantity.toString()
            itemUnit.text = currentItem.unit
            checkBox.isChecked = currentItem.isChecked
            setStrikethrough(holder.itemView, currentItem.isChecked)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                itemManager.updateItem(currentItem, isChecked)
                setStrikethrough(holder.itemView, isChecked)
            }

            swipeLayout.visibility = if (position == swipedPosition) View.VISIBLE else View.GONE

            if(renamePosition == position){
                showRenameLayout(position, currentItem, context, itemTitle, checkBox, editItemLayout, editItemTitle, editItemQuantity, editItemUnit, units)
            } else {
                hideRenameLayout(editItemLayout, itemTitle, checkBox, editItemTitle)
            }

            btnRename.setOnClickListener {
                showRenameLayout(position)
            }

            btnDelete.setOnClickListener {
                deleteItem(position)
                swipedPosition = -1
            }
        }
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

    private fun setStrikethrough(context: View, isChecked: Boolean) {
        val strikethrougLine = context.findViewById<View>(R.id.itemStrikethroughLine)
        if (isChecked) {
            strikethrougLine.visibility = View.VISIBLE
        } else {
            strikethrougLine.visibility = View.GONE
        }
    }

    fun getRenamePosition(): Int {
        return renamePosition
    }

    private fun showRenameLayout(position: Int) {
        renamePosition = position
        notifyItemChanged(position)
    }

    private fun showRenameLayout(position: Int, currentItem: ListItem, context: Context, itemTitle: TextView, checkBox: CheckBox, editItemLayout: LinearLayout, editItemTitle: TextInputEditText, editItemQuantity: TextInputEditText, editItemUnit: Spinner, units: Array<String>){
        editItemLayout.visibility = View.VISIBLE
        itemTitle.visibility = View.GONE
        checkBox.visibility = View.GONE

        editItemTitle.hint = currentItem.title
        editItemQuantity.hint = currentItem.quantity.toString()

        editItemTitle.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                Tools.showKeyboard(v as TextView)
            } else {
                Tools.hideKeyboard(v as TextView)
            }
        }

        editItemQuantity.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                Tools.showKeyboard(v as TextView)
            } else {
                Tools.hideKeyboard(v as TextView)
            }
        }

        val adapter = object : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, units) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getView(position, convertView, parent) as TextView
                textView.setTypeface(null, Typeface.BOLD)
                return textView
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getDropDownView(position, convertView, parent) as TextView
                textView.setTypeface(null, Typeface.BOLD)
                return textView
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editItemUnit.adapter = adapter
        editItemUnit.setSelection(units.indexOf(currentItem.unit))


        editItemTitle.setOnEditorActionListener { v, actionId, _ ->
            if (Tools.getKeyboardEnter(actionId)) {
                if(editItem(currentItem, position, editItemTitle, editItemQuantity, editItemUnit)){ Tools.hideKeyboard(v) }
                true
            } else {
                false
            }
        }

        editItemQuantity.setOnEditorActionListener { v, actionId, _ ->
            if (Tools.getKeyboardEnter(actionId)) {
                if(editItem(currentItem, position, editItemTitle, editItemQuantity, editItemUnit)){ Tools.hideKeyboard(v) }
                true
            } else {
                false
            }
        }
    }

    private fun hideRenameLayout(renameLayout: LinearLayout, itemTitle: TextView, checkBox: CheckBox, renameEditText: TextInputEditText){
        renameLayout.visibility = View.GONE
        itemTitle.visibility = View.VISIBLE
        checkBox.visibility = View.VISIBLE

        renameEditText.text!!.clear()
        renameEditText.clearFocus()
    }

    private fun editItem(currentItem: ListItem, position: Int, editItemTitle: TextInputEditText, editItemQuantity: TextInputEditText, editItemUnit: Spinner): Boolean{

        val newName = editItemTitle.text.toString()
        val title = newName.ifEmpty {
            currentItem.title
        }

        val newQuantityString = editItemQuantity.text.toString()
        val quantity = if(newQuantityString.isEmpty()){
            currentItem.quantity
        } else {
            newQuantityString.toIntOrNull() ?: 1
        }

        val unit = editItemUnit.selectedItem.toString()

        itemManager.editItem(position, ListItem(title, quantity, unit, currentItem.isChecked))

        editItemTitle.text!!.clear()
        editItemQuantity.text!!.clear()
        editItemTitle.clearFocus()
        editItemQuantity.clearFocus()

        renamePosition = -1
        notifyItemChanged(position)

        return true
    }
}


