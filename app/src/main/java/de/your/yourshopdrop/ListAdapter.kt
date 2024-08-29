package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

//TODO: Standardliste wird automatisch umbenannt wenn die Sprache gewechselt wird
class ListAdapter(private val itemManager: ItemManager, private val itemAdapter: ItemAdapter, private val screenManager: ScreenManager, private val activity: Activity) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    private var swipedPosition = -1
    private var renamePosition = -1

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @SuppressLint("NotifyDataSetChanged")
    fun add(listName: String) {
        itemManager.createNewList(listName)
        notifyItemInserted(itemCount - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val holder = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_item_open_more, parent, false))
        return holder
    }

    override fun getItemCount(): Int {
        return itemManager.listAllLists().size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentList = itemManager.getList(position)

        holder.itemView.apply {
            val listTitle = findViewById<TextView>(R.id.template_item_title_more)
            val btnShowList = findViewById<ImageButton>(R.id.btnOpenMore)
            val swipeLayout = findViewById<LinearLayout>(R.id.swipeLayout)
            val renameLayout = findViewById<TextInputLayout>(R.id.editItemContainer)
            val renameEditText = findViewById<TextInputEditText>(R.id.editItemTitle)
            val btnRename = findViewById<ImageButton>(R.id.btnSwipeRename)
            val btnDelete = findViewById<ImageButton>(R.id.btnSwipeDelete)

            btnShowList.contentDescription = activity.getString(R.string.description_OpenList) + ": " + currentList

            listTitle.text = currentList

            swipeLayout.visibility = if (position == swipedPosition) View.VISIBLE else View.GONE

            if(renamePosition == position){
                renameLayout.visibility = View.VISIBLE
                renameEditText.setOnFocusChangeListener { _, hasFocus ->
                    if(hasFocus){
                        renameEditText.hint = listTitle.text.toString()
                    } else {
                        renameEditText.hint = ""
                    }
                }
            } else {
                renameLayout.visibility = View.GONE

                renameEditText.text!!.clear()
                renameEditText.clearFocus()
            }

            btnShowList.setOnClickListener {
                itemManager.setCurrentList(currentList)
                itemAdapter.refreshList()

                screenManager.hideScreen()
            }

            btnRename.setOnClickListener {
                showRenameLayout(position)
            }

            btnDelete.setOnClickListener {
                deleteList(position)
                swipedPosition = -1
            }

            renameEditText.setOnEditorActionListener { v, actionId, _ ->
                if (Tools.getKeyboardEnter(actionId)) {
                    val newName = renameEditText.text.toString()

                    if(newName.isNotEmpty()){
                        itemManager.renameList(listTitle.text.toString(), newName)
                    }

                    renamePosition = -1

                    renameEditText.text!!.clear()
                    renameEditText.clearFocus()

                    notifyItemChanged(position)
                    Tools.hideKeyboard(v)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun deleteList(position: Int) {
        itemManager.deleteList(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        activity.findViewById<TextView>(R.id.tvListTitle).text = itemManager.getCurrentListName()
        itemAdapter.refreshList()
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

    fun getRenamePosition(): Int {
        return renamePosition
    }

    private fun showRenameLayout(position: Int) {
        renamePosition = position
        notifyItemChanged(position)
    }
}


