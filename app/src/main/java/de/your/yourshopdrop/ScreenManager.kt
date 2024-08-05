package de.your.yourshopdrop

import android.app.Activity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout


class ScreenManager (private val context: Activity, private val listAdapter: ListAdapter? = null){

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

    private fun hideScreen(screen: PopupWindow?){
        screen?.dismiss()
        onScreenClose()
        currentActivePopup = null
    }

    private fun screenAddItem(): ScreenInflater.Screen {
        requireNotNull(listAdapter) { "ListAdapter darf nicht null sein" }

        val inflatedScreen = screenInflater.createScreen(R.layout.screen_additem)

        val editText: EditText = inflatedScreen.screenView.findViewById(R.id.input_new_item)

        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()) {
                    listAdapter.add(ListItem(editText.text.toString()))
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

    //TODO: Funktionalität für Auswahl von Listen
    private fun screenLists() : ScreenInflater.Screen {
        val inflatedScreen = screenInflater.createScreen(R.layout.screen_lists)

        val btnClose: ImageButton = inflatedScreen.screenView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(inflatedScreen.popupWindow)
        }

        val btnAddList : ImageButton = inflatedScreen.screenView.findViewById(R.id.btnAddList)
        btnAddList.setOnClickListener {

        }

        return inflatedScreen
    }

    fun onScreenClose(){
        val selectedItemSettings: ConstraintLayout = context.findViewById(R.id.selectedNavbarItem_Settings)
        val selectedItemLists: ConstraintLayout = context.findViewById(R.id.selectedNavbarItem_Lists)
        selectedItemSettings.visibility = View.GONE
        selectedItemLists.visibility = View.GONE

        Tools.removeBlur(context.findViewById(R.id.scrollViewListItems))
    }
}