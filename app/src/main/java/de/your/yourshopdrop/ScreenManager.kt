package de.your.yourshopdrop

import android.app.Activity
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText


class ScreenManager (private val context: Activity, private val itemAdapter: ItemAdapter, private val itemManager: ItemManager, private val activity: MainActivity){
    private lateinit var addItemTitle : EditText
    private lateinit var addItemQuantity : EditText
    private lateinit var addItemUnit : Spinner

    private var currentActiveScreen: Screen = Screen.START
    private lateinit var inflatedScreen: ScreenInflater.Screen

    private val screenInflater = ScreenInflater(context, this)
    private var currentActivePopup: PopupWindow? = null



    fun showScreen(screen: Screen){

        hideScreen(currentActivePopup)

        val selectedItemSettings = context.findViewById<ConstraintLayout>(R.id.selectedNavbarItem_Settings)
        val selectedItemLists = context.findViewById<ConstraintLayout>(R.id.selectedNavbarItem_Lists)
        val scrollViewListItems = context.findViewById<View>(R.id.scrollViewListItems)


        when(screen){
            Screen.ADD_ITEM -> {
                inflatedScreen = screenAddItem()
                currentActiveScreen = Screen.ADD_ITEM
                Tools.blurView(scrollViewListItems)
            }
            Screen.SETTINGS -> {
                inflatedScreen = screenSettings()
                selectedItemSettings.visibility = View.VISIBLE
                currentActiveScreen = Screen.SETTINGS
                Tools.blurView(scrollViewListItems)
            }
            Screen.LISTS -> {
                inflatedScreen = screenLists()
                selectedItemLists.visibility = View.VISIBLE
                currentActiveScreen = Screen.LISTS
                Tools.blurView(scrollViewListItems)
            }
            Screen.START -> {
                //TODO: Startscreen zeigen
                inflatedScreen = screenAddItem()
                currentActiveScreen = Screen.START
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

        addItemTitle = inflatedScreen.screenView.findViewById(R.id.input_new_item)
        addItemQuantity = inflatedScreen.screenView.findViewById(R.id.input_quantity)
        addItemUnit = inflatedScreen.screenView.findViewById(R.id.spinner_unit)

        val units = activity.resources.getStringArray(R.array.quantity_units)
        val adapter = object : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, units) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getView(position, convertView, parent) as TextView
                textView.setTypeface(null, Typeface.BOLD)
                return textView
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getDropDownView(position, convertView, parent) as TextView
                textView.setTypeface(null, Typeface.BOLD)
                return textView
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        addItemUnit.adapter = adapter

        addItemTitle.setOnEditorActionListener { v, actionId, _ ->
            if (Tools.getKeyboardEnter(actionId)) {
                if (addItem()) {
                    hideScreen(inflatedScreen.popupWindow)
                    Tools.hideKeyboard(v)
                }
                true
            } else {
                false
            }
        }

        addItemQuantity.setOnEditorActionListener { v, actionId, _ ->
            if (Tools.getKeyboardEnter(actionId)) {
                if (addItem()) {
                    hideScreen(inflatedScreen.popupWindow)
                    Tools.hideKeyboard(v)
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

    private fun screenSettings() : ScreenInflater.Screen {
        val inflatedScreen = screenInflater.createScreen(R.layout.screen_settings)

        val btnClose: ImageButton = inflatedScreen.screenView.findViewById(R.id.btnCloseScreen)
        btnClose.setOnClickListener {
            hideScreen(inflatedScreen.popupWindow)
        }

        val languageSpinner: Spinner = inflatedScreen.screenView.findViewById(R.id.languageSpinner)

        LanguageManager(activity).setupSpinnerListener(languageSpinner, this)

        return inflatedScreen
    }

    private fun screenLists() : ScreenInflater.Screen {
        val inflatedScreen = screenInflater.createScreen(R.layout.screen_lists)

        val listAdapter = ListAdapter(itemManager, itemAdapter, this, context)

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
        checkInflatedScreen()

        Tools.removeBlur(context.findViewById(R.id.scrollViewListItems))
    }

    private fun checkInflatedScreen(){
        when(currentActiveScreen){
            Screen.ADD_ITEM -> {
                addItem()
            }
            Screen.LISTS -> {
                context.findViewById<TextView>(R.id.tvListTitle).text = itemManager.getCurrentListName()
            }
            else -> { }
        }
    }

    private fun addItem() : Boolean{
        val title = addItemTitle.text
        if (title.isEmpty()) { return false }

        val quantity: Int = addItemQuantity.text.toString().toIntOrNull() ?: 1

        val unit = addItemUnit.selectedItem.toString()

        itemAdapter.add(ListItem(title.toString(), quantity, unit))
        addItemTitle.text.clear()
        addItemQuantity.text.clear()

        return true
    }
}