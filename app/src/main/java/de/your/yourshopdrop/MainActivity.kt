package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale




class MainActivity : AppCompatActivity() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var itemManager: ItemManager
    private lateinit var screenManager: ScreenManager
    private lateinit var languageManager: LanguageManager

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemManager = ItemManager(this)
        itemAdapter = ItemAdapter(itemManager)
        screenManager = ScreenManager(this, itemAdapter, itemManager, this)
        languageManager = LanguageManager(this)

        languageManager.applySavedLanguageOnStart()

        enableEdgeToEdge()
        setContentView(R.layout.screen_main)
        Tools.defineSafeWindow(this)

        setupItems()

        val btnAddItem : ImageButton = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener{
            screenManager.showScreen(Screen.ADD_ITEM)
        }

        val btnSettings : ImageButton = findViewById(R.id.btnShowSettings)
        btnSettings.setOnClickListener{
            screenManager.showScreen(Screen.SETTINGS)
        }

        val btnLists : ImageButton = findViewById(R.id.btnShowLists)
        btnLists.setOnClickListener {
            screenManager.showScreen(Screen.LISTS)
        }

    }

    private fun setupItems(){
        val rvItemList : RecyclerView = findViewById(R.id.rvItemList)
        rvItemList.adapter = itemAdapter
        rvItemList.layoutManager = LinearLayoutManager(this)

        val tvListTitle: TextView = findViewById(R.id.tvListTitle)
        tvListTitle.text = itemManager.getCurrentListName()

        val itemTouchHelper = ItemTouchHelper(SwipeActions(itemAdapter))
        itemTouchHelper.attachToRecyclerView(rvItemList)
        rvItemList.addOnItemTouchListener(RecyclerViewTouchListener(this, rvItemList, itemAdapter))
    }

    public fun changeLanguage(language: String) {
        val locale = when (language) {
            "Deutsch" -> Locale("de")
            "Français" -> Locale("fr")
            "English" -> Locale("en")
            else -> Locale("en")
        }
        Locale.setDefault(locale)
        val config: Configuration = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        recreate()
    }
}