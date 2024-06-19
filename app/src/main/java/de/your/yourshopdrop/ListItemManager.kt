package de.your.yourshopdrop

import android.content.Context

class ListItemManager(context: Context, filename: String) {
    var saveManager:SaveManager = SaveManager(context, filename)

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

    fun deleteAllItems(){
        saveManager.deleteList()
    }

    fun getItemCount():Int{
        return loadItems().size
    }

    fun getItem(position: Int): ListItem{
        val items: MutableList<ListItem> = loadItems()
        return items[position]
    }

    fun getItemPosition(item: ListItem):Int{
        val items: MutableList<ListItem> = loadItems()
        return items.indexOf(item)
    }
}