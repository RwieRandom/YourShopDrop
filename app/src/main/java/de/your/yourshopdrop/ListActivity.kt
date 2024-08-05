package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListActivity : AppCompatActivity() {
    private lateinit var listAdapter: ListAdapter
    private lateinit var listItemManager: ListItemManager
    private lateinit var screenInflater: ScreenInflater
    private lateinit var screenManager: ScreenManager

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listItemManager = ListItemManager(this, "items.json")
        listAdapter = ListAdapter(listItemManager)
        screenManager = ScreenManager(this, listAdapter)


        enableEdgeToEdge()
        setContentView(R.layout.screen_main)
        Tools.defineSafeWindow(this)

        setupList()

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

    private fun setupList(){
        val rvItemList : RecyclerView = findViewById(R.id.rvItemList)
        rvItemList.adapter = listAdapter
        rvItemList.layoutManager = LinearLayoutManager(this)
        val tvListTitle: TextView = findViewById(R.id.tvListTitle)


        val itemTouchHelper = ItemTouchHelper(SwipeActions(listAdapter))
        itemTouchHelper.attachToRecyclerView(rvItemList)
        rvItemList.addOnItemTouchListener(RecyclerTouchListener(this, rvItemList, listAdapter))
    }
}