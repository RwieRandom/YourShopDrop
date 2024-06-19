package de.your.yourshopdrop

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException

class SaveManager(val context: Context, val filename: String) {

    val gson = Gson()

    /**
     * Speichert eine MutableList als JSON-Datei.
     * @param list Die zu speichernde Liste.
     */
    inline fun <reified T> saveList(list: MutableList<T>) {
        val jsonString = gson.toJson(list)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    /**
     * Lädt eine MutableList aus einer JSON-Datei.
     * @return Die geladene Liste oder eine leere Liste, falls die Datei nicht gefunden wurde oder ein Fehler auftrat.
     */
    inline fun <reified T> loadList(): MutableList<T> {
        return try {
            val jsonFileString = context.openFileInput(filename).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<T>>() {}.type
            gson.fromJson<List<T>>(jsonFileString, listType).toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    /**
     * Fügt ein Element zu einer gespeicherten Liste hinzu.
     * @param item Das hinzuzufügende Element.
     */
    inline fun <reified T> addToList(item: T) {
        val list = loadList<T>()
        list.add(item)
        saveList(list)
    }

    /**
     * Entfernt ein Element aus einer gespeicherten Liste.
     * @param item Das zu entfernende Element.
     */
    inline fun <reified T> removeFromList(item: T) {
        val list = loadList<T>()
        list.remove(item)
        saveList(list)
    }

    /**
     * Löscht die gespeicherte Liste.
     */
    fun deleteList() {
        context.deleteFile(filename)
    }
}