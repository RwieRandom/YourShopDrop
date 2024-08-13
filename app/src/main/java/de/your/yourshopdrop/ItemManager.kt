package de.your.yourshopdrop

import android.content.Context
import android.content.SharedPreferences

class ItemManager(context: Context) : SaveManager(context) {

    private var currentListName: String? = null
    private var defaultListName = context.getString(R.string.placeholder_title)
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
        saveCurrentList()
        // Wenn die Liste nicht existiert, erstelle sie
        if (!listAllLists().contains(listName)) {
            createNewList(listName)
        }
    }

    private fun saveCurrentList(){
        currentListName?.let {
            preferences.edit().putString(lastUsedListKey, it).apply()
        }
    }

    override fun createNewList(listName: String): Boolean {
        val result = super.createNewList(listName)
        if (result) {
            setCurrentList(listName)
        }
        return result
    }

    fun deleteList(position: Int) {
        val listName : String = getList(position)
        deleteList(listName)
    }

    override fun deleteList(listName: String) {
        super.deleteList(listName)
        if (currentListName == listName) {
            val allLists = listAllLists()

            val otherLists = allLists.filter { it != listName }
            if (otherLists.isNotEmpty()) {
                setCurrentList(otherLists.first())
            } else {
                defaultListName = context.getString(R.string.placeholder_title)
                setCurrentList(defaultListName)
            }
        }
    }

    override fun renameList(oldListName: String, newListName: String) {
        super.renameList(oldListName, newListName)
        if (currentListName == oldListName) {
            setCurrentList(newListName)
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

    private fun loadItems(): MutableList<ListItem> {
        return currentListName?.let {
            loadList(it)
        } ?: mutableListOf()
    }

    fun addItem(item: ListItem) {
        currentListName?.let {
            addToList(it, item)
        }
    }

    fun removeItem(position: Int) {
        removeFromList(currentListName ?: return, getItem(position))
    }

    fun editItem(position: Int, newItem: ListItem) {
        val oldItem = getItem(position)
        editItemInList(currentListName ?: return, oldItem, newItem)
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

    fun getList(position: Int): String {
        val lists = listAllLists()
        return lists[position]
    }
}
