package de.your.yourshopdrop

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ScreenManager (private val context: Activity, private val itemAdapter: ItemAdapter, private val itemManager: ItemManager){

    private val screenInflater = ScreenInflater(context)

    private var currentActivePopup: PopupWindow? = null

    fun showScreen(screen: Screen){

        val inflatedScreen: ScreenInflater.Screen
        hideScreen(currentActivePopup)

        val selectedItemSettings = context.findViewById<ConstraintLayout>(R.id.selectedNavbarItem_Settings)
        val selectedItemLists = context.findViewById<ConstraintLayout>(R.id.selectedNavbarItem_Lists)
        val scrollViewListItems = context.findViewById<View>(R.id.scrollViewListItems)


        when(screen){
            Screen.ADD_ITEM -> {
                inflatedScreen = screenAddItem()

                Tools.blurView(scrollViewListItems)
            }
            Screen.SETTINGS -> {
                inflatedScreen = screenSettings()
                selectedItemSettings.visibility = View.VISIBLE

                Tools.blurView(scrollViewListItems)
            }
            Screen.LISTS -> {
                inflatedScreen = screenLists()
                selectedItemLists.visibility = View.VISIBLE

                Tools.blurView(scrollViewListItems)
            }
            Screen.START -> {
                //TODO: Startscreen zeigen
                inflatedScreen = screenAddItem()
            }
        }

        currentActivePopup = inflatedScreen.popupWindow
    }

    fun hideScreen(){
        hideScreen(currentActivePopup)
    }

    private fun hideScreen(screen: PopupWindow?){
        screen?.dismiss()
        onScreenClose()
        currentActivePopup = null
    }

    private fun screenAddItem(): ScreenInflater.Screen {

        val inflatedScreen = screenInflater.createScreen(R.layout.screen_additem)

        val editText: EditText = inflatedScreen.screenView.findViewById(R.id.input_new_item)

        editText.setOnEditorActionListener { v, actionId, _ ->
            if (Tools.getKeyboardEnter(actionId)) {
                if (editText.text.isNotEmpty()) {
                    itemAdapter.add(ListItem(editText.text.toString()))
                    editText.text.clear()
                    hideScreen(inflatedScreen.popupWindow)
                    Tools.hideKeyboard(v)
                } else {
                    hideScreen(inflatedScreen.popupWindow)
                }
                true
            } else {
                false
            }
        }

        val btnClose: ImageButton = inflatedScreen.screenView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(inflatedScreen.popupWindow)
        }

        return inflatedScreen
    }

    //TODO: Funktionalität für Settings z.B Sprachauswahl
    private fun screenSettings() : ScreenInflater.Screen {
        val inflatedScreen = screenInflater.createScreen(R.layout.screen_settings)

        val btnClose: ImageButton = inflatedScreen.screenView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(inflatedScreen.popupWindow)
        }

        return inflatedScreen
    }

    private fun screenLists() : ScreenInflater.Screen {
        val inflatedScreen = screenInflater.createScreen(R.layout.screen_lists)

        val listAdapter = ListAdapter(inflatedScreen.screenView, itemManager, itemAdapter, this, context)

        val screenList : RecyclerView = inflatedScreen.screenView.findViewById(R.id.rvScreenList)
        screenList.adapter = listAdapter
        screenList.layoutManager = LinearLayoutManager(context)

        val itemTouchHelper = ItemTouchHelper(SwipeActions(listAdapter))
        itemTouchHelper.attachToRecyclerView(screenList)
        screenList.addOnItemTouchListener(RecyclerViewTouchListener(context, screenList, listAdapter))

        val btnClose: ImageButton = inflatedScreen.screenView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(inflatedScreen.popupWindow)
        }

        val btnAddList : ImageButton = inflatedScreen.screenView.findViewById(R.id.btnAddList)
        btnAddList.setOnClickListener {
            val input = inflatedScreen.screenView.findViewById<EditText>(R.id.input_new_list)

            input.visibility = View.VISIBLE
            btnAddList.visibility = View.GONE
            input.requestFocus()
            Tools.showKeyboard(input)

            input.setOnEditorActionListener { v, actionId, _ ->
                if (Tools.getKeyboardEnter(actionId)) {
                    if (input.text.isNotEmpty()) {
                        listAdapter.add(input.text.toString())
                        input.text.clear()
                        Tools.hideKeyboard(v)
                        input.visibility = View.GONE
                        btnAddList.visibility = View.VISIBLE
                    } else {
                        hideScreen(inflatedScreen.popupWindow)
                    }
                    true
                } else {
                    false
                }
            }
        }

        return inflatedScreen
    }

    fun onScreenClose(){
        val selectedItemSettings: ConstraintLayout = context.findViewById(R.id.selectedNavbarItem_Settings)
        val selectedItemLists: ConstraintLayout = context.findViewById(R.id.selectedNavbarItem_Lists)
        selectedItemSettings.visibility = View.GONE
        selectedItemLists.visibility = View.GONE

        context.findViewById<TextView>(R.id.tvListTitle).text = itemManager.getCurrentListName()
        Tools.removeBlur(context.findViewById(R.id.scrollViewListItems))
    }
}