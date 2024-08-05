package de.your.yourshopdrop

import android.content.Context
import android.content.SharedPreferences

class ItemManager(context: Context) : SaveManager(context) {

    private var currentListName: String? = null
    private val defaultListName = "defaultList"
    private val preferences: SharedPreferences = context.getSharedPreferences("ItemManagerPrefs", Context.MODE_PRIVATE)
    private val lastUsedListKey = "lastUsedList"

    init {
        // Beim Erstellen des ItemManagers die letzte verwendete Liste abrufen oder Standardliste verwenden
        val lastUsedList = preferences.getString(lastUsedListKey, null)
        val allLists = listAllLists()
        if (lastUsedList != null && allLists.contains(lastUsedList)) {
            setCurrentList(lastUsedList)
        } else {
            if (allLists.isEmpty()) {
                // Wenn keine Listen vorhanden sind, eine Standardliste erstellen
                createNewList(defaultListName)
            }
            setCurrentList(defaultListName)
        }
    }

    /**
     * Setzt die aktuelle Liste, die geladen werden soll.
     * @param listName Der Name der Liste, die geladen werden soll.
     */
    fun setCurrentList(listName: String) {
        currentListName = listName

        // Wenn die Liste nicht existiert, erstelle sie
        if (!listAllLists().contains(listName)) {
            createNewList(listName)
        }
    }

    fun getCurrentListName(): String? {
        return currentListName
    }

    private fun saveItems(list: MutableList<ListItem>) {
        currentListName?.let {
            saveList(it, list)
        }
    }

    fun loadItems(): MutableList<ListItem> {
        return currentListName?.let {
            loadList(it)
        } ?: mutableListOf()
    }

    fun addItem(item: ListItem) {
        currentListName?.let {
            addToList(it, item)
        }
    }

    fun removeItem(item: ListItem) {
        currentListName?.let {
            removeFromList(it, item)
        }
    }

    fun removeItem(position: Int) {
        currentListName?.let {
            val items = loadItems()
            items.removeAt(position)
            saveItems(items)
        }
    }

    fun renameItem(position: Int, newName: String) {
        currentListName?.let {
            val items = loadItems()
            items[position].title = newName
            saveItems(items)
        }
    }

    fun deleteAllItems() {
        currentListName?.let {
            deleteList(it)
        }
    }

    fun updateItem(item: ListItem, isChecked: Boolean) {
        currentListName?.let {
            val items = loadItems()
            val position = items.indexOf(item)
            if (position != -1) {
                items[position].isChecked = isChecked // Update the item
                saveItems(items) // Save the changes
            }
        }
    }

    fun getItemCount(): Int {
        return loadItems().size
    }

    fun getItem(position: Int): ListItem {
        val items: MutableList<ListItem> = loadItems()
        return items[position]
    }
}
