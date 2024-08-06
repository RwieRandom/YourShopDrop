package de.your.yourshopdrop

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

open class SaveManager(val context: Context) {

    val gson = Gson()
    private val fileExtension = ".json"

    /**
     * Fügt die Dateiendung zum Listennamen hinzu.
     * @param listName Der Name der Liste.
     * @return Der vollständige Dateiname.
     */
    fun getFilename(listName: String): String {
        return listName + fileExtension
    }

    /**
     * Erstellt eine neue leere Liste.
     * @param listName Der Name der neuen Liste.
     * @return true, wenn die Liste erfolgreich erstellt wurde, false, wenn die Liste bereits existiert.
     */
    open fun createNewList(listName: String): Boolean {
        val filename = getFilename(listName)
        val file = File(context.filesDir, filename)
        return if (file.exists()) {
            false
        } else {
            saveList(listName, mutableListOf<Any>())
            true
        }
    }

    /**
     * Speichert eine MutableList als JSON-Datei.
     * @param listName Der Name der zu speichernden Liste.
     * @param list Die zu speichernde Liste.
     */
    inline fun <reified T> saveList(listName: String, list: MutableList<T>) {
        val jsonString = gson.toJson(list)
        context.openFileOutput(getFilename(listName), Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    /**
     * Lädt eine MutableList aus einer JSON-Datei.
     * @param listName Der Name der zu ladenden Liste.
     * @return Die geladene Liste oder eine leere Liste, falls die Datei nicht gefunden wurde oder ein Fehler auftrat.
     */
    inline fun <reified T> loadList(listName: String): MutableList<T> {
        return try {
            val jsonFileString = context.openFileInput(getFilename(listName)).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<T>>() {}.type
            gson.fromJson<List<T>>(jsonFileString, listType).toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    /**
     * Fügt ein Element zu einer gespeicherten Liste hinzu.
     * @param listName Der Name der Liste, zu der das Element hinzugefügt werden soll.
     * @param item Das hinzuzufügende Element.
     */
    inline fun <reified T> addToList(listName: String, item: T) {
        val list = loadList<T>(listName)
        list.add(item)
        saveList(listName, list)
    }

    /**
     * Entfernt ein Element aus einer gespeicherten Liste.
     * @param listName Der Name der Liste, aus der das Element entfernt werden soll.
     * @param item Das zu entfernende Element.
     */
    inline fun <reified T> removeFromList(listName: String, item: T) {
        val list = loadList<T>(listName)
        list.remove(item)
        saveList(listName, list)
    }

    /**
     * Löscht eine gespeicherte Liste.
     * @param listName Der Name der zu löschenden Liste.
     */
    open fun deleteList(listName: String) {
        context.deleteFile(getFilename(listName))
    }

    /**
     * Listet alle gespeicherten Listen auf.
     * @return Eine Liste der Namen aller gespeicherten Listen.
     */
    fun listAllLists(): List<String> {
        val files = context.filesDir.listFiles()
        return files?.filter { it.name.endsWith(fileExtension) }
            ?.map { it.name.removeSuffix(fileExtension) }
            ?: emptyList()
    }

    /**
     * Benennt ein Element in einer gespeicherten Liste um.
     * @param listName Der Name der Liste, in der das Element umbenannt werden soll.
     * @param oldItem Das zu ändernde Element.
     * @param newItem Der neue Wert des Elements.
     */
    inline fun <reified T> renameItemInList(listName: String, oldItem: T, newItem: T) {
        val list = loadList<T>(listName)
        val index = list.indexOf(oldItem)
        if (index != -1) {
            list[index] = newItem
            saveList(listName, list)
        }
    }

    /**
     * Benennt eine gespeicherte Liste um.
     * @param oldListName Der aktuelle Name der Liste.
     * @param newListName Der neue Name der Liste.
     * @return true, wenn die Liste erfolgreich umbenannt wurde, false, wenn die Liste mit dem neuen Namen bereits existiert.
     */
    open fun renameList(oldListName: String, newListName: String) {
        val oldFilename = getFilename(oldListName)
        val newFilename = getFilename(newListName)
        val oldFile = File(context.filesDir, oldFilename)
        val newFile = File(context.filesDir, newFilename)
        if (!newFile.exists()) {
            oldFile.renameTo(newFile)
        }
    }
}
