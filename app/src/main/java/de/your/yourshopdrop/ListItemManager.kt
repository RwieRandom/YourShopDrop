package de.your.yourshopdrop

import android.content.Context

class ListItemManager(context: Context, filename: String) {
    private var saveManager:SaveManager = SaveManager(context, filename)

    private fun saveItems(list: MutableList<ListItem>) {
        saveManager.saveList(list)
    }

    fun loadItems(): MutableList<ListItem>{
        return saveManager.loadList()
    }

    fun addItem(item: ListItem){
        saveManager.addToList(item)
    }

    fun removeItem(item: ListItem){
        saveManager.removeFromList(item)
    }

    fun removeItem(position: Int){
        removeItem(getItem(position))
    }

    fun renameItem(position: Int, newName: String){
        val items = loadItems()
        items[position].title = newName
        saveItems(items)
    }

    fun renameItem(item: ListItem, newName: String) {
        renameItem(loadItems().indexOf(item), newName)
    }

    fun deleteAllItems(){
        saveManager.deleteList()
    }

    fun updateItem(item: ListItem, isChecked: Boolean) {
        val items = loadItems()
        val position = items.indexOf(item)
        if (position != -1) {
            items[position].isChecked = isChecked // Update the item
            saveItems(items) // Save the changes
        }
    }

    fun getItemCount():Int{
        return loadItems().size
    }

    fun getItem(position: Int): ListItem{
        val items: MutableList<ListItem> = loadItems()
        return items[position]
    }

    /*fun getItemPosition(item: ListItem):Int{
        val items: MutableList<ListItem> = loadItems()
        return items.indexOf(item)
    }*/
}