package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListActivity : AppCompatActivity() {
    private lateinit var listAdapter: ListAdapter
    private lateinit var listItemManager: ListItemManager
    private lateinit var popupManager: PopupManager

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        popupManager = PopupManager(this)
        listItemManager = ListItemManager(this, "items.json")
        listAdapter = ListAdapter(listItemManager)


        enableEdgeToEdge()
        setContentView(R.layout.screen_main)
        Tools.defineSafeWindow(this)

        setupList()

        val btnAddItem : ImageButton = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener{
            showScreen(Screen.ADD_ITEM)
        }

        val btnSettings : ImageButton = findViewById(R.id.btnShowSettings)
        btnSettings.setOnClickListener{
            showScreen(Screen.SETTINGS)
        }

        val btnLists : ImageButton = findViewById(R.id.btnShowLists)
        btnLists.setOnClickListener {
            showScreen(Screen.LISTS)
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


    private var currentActivePopup: PopupWindow? = null

    //TODO: ScreenManager
    private fun showScreen(screen: Screen){

        val popup: PopupManager.Popup
        hideScreen(currentActivePopup)

        val selectedItemSettings: ConstraintLayout = findViewById(R.id.selectedNavbarItem_Settings)
        val selectedItemLists: ConstraintLayout = findViewById(R.id.selectedNavbarItem_Lists)
        selectedItemSettings.visibility = View.GONE
        selectedItemLists.visibility = View.GONE

        Tools.blurView(findViewById(R.id.scrollViewListItems))

        when(screen){
            Screen.ADD_ITEM -> {
                popup = createPopupAddItem()
            }
            Screen.SETTINGS -> {
                popup = createPopupSettings()
                selectedItemSettings.visibility = View.VISIBLE
            }
            Screen.LISTS -> {
                popup = createPopupLists()
                selectedItemLists.visibility = View.VISIBLE
            } else -> {
                //TODO: Startscreen zeigen
                popup = createPopupAddItem()
            }
        }

        currentActivePopup = popup.popupWindow
    }

    private fun hideScreen(screen: PopupWindow?){
        if (screen == null) return

        screen.dismiss()
        val selectedItemSettings: ConstraintLayout = findViewById(R.id.selectedNavbarItem_Settings)
        val selectedItemLists: ConstraintLayout = findViewById(R.id.selectedNavbarItem_Lists)
        selectedItemSettings.visibility = View.GONE
        selectedItemLists.visibility = View.GONE

        Tools.removeBlur(findViewById(R.id.scrollViewListItems))

        currentActivePopup = null
    }


    private fun createPopupAddItem(): PopupManager.Popup {
        val popup = popupManager.createPopup(R.layout.screen_additem)

        val editText: EditText = popup.popupView.findViewById(R.id.input_new_item)

//        val btnConfirm: Button = popup.popupView.findViewById(R.id.btnScreenConfirm)
//        btnConfirm.setOnClickListener {
//
//        }

        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()) {
                    listAdapter.add(ListItem(editText.text.toString()))
                    editText.text.clear()
                    hideScreen(popup.popupWindow)
                    Tools.hideKeyboard(v)
                } else {
                    hideScreen(popup.popupWindow)
                }
                true
            } else {
                false
            }
        }

//        val btnCancel: Button = popup.popupView.findViewById(R.id.btnScreenCancel)
//        btnCancel.setOnClickListener {
//            hideScreen(popup.popupWindow)
//        }

        val btnClose: ImageButton = popup.popupView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(popup.popupWindow)
        }

        return popup
    }

    //TODO: Funktionalität für Settings z.B Sprachauswahl
    private fun createPopupSettings() : PopupManager.Popup {
        val popup = popupManager.createPopup(R.layout.screen_settings)

        val btnClose: ImageButton = popup.popupView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(popup.popupWindow)
        }

        return popup
    }

    //TODO: Funktionalität für Auswahl von Listen
    private fun createPopupLists() : PopupManager.Popup {
        val popup = popupManager.createPopup(R.layout.screen_lists)

        val btnClose: ImageButton = popup.popupView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(popup.popupWindow)
        }

        val btnAddList : ImageButton = popup.popupView.findViewById(R.id.btnAddList)
        btnAddList.setOnClickListener {

        }

        return popup
    }
}