package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListActivity : AppCompatActivity() {
    private lateinit var listAdapter: ListAdapter

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val popupManager = PopupManager(getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,  this)
        val listItemManager = ListItemManager(this, "items.json")
        listAdapter = ListAdapter(listItemManager)


        enableEdgeToEdge()
        setContentView(R.layout.list_activity)
        defineSafeWindow()

        /*
        //TODO: Darkmode/Lightmode

        // Automatischer Wechsel zwischen Light und Dark Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        */

        setupList()

        val btnAddItem : Button = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener{
            showInputLine()
        }

        val btnOpenMore: ImageButton = findViewById(R.id.btnMore)
        btnOpenMore.setOnClickListener{
            createMorePopup(popupManager, btnOpenMore)
        }
    }

    private fun setupList(){
        val rvItemList : RecyclerView = findViewById(R.id.rvItemList)
        rvItemList.adapter = listAdapter
        rvItemList.layoutManager = LinearLayoutManager(this)
        val tvListTitle: TextView = findViewById(R.id.tvListTitle)
        tvListTitle.text = loadTitle()
    }

    private fun showInputLine(){
        //TODO: animations
        val inputItem: View = findViewById(R.id.includeInputItem)
        inputItem.visibility = View.VISIBLE

        val editText: EditText = findViewById(R.id.etItemTitleInput)
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val title = editText.text.toString()
                if(title.isNotEmpty()){
                    listAdapter.add(ListItem(title))
                    hideEditText(inputItem, editText)
                } else {
                    hideEditText(inputItem, editText)
                }
                true
            } else { false}
        }
    }

    private fun hideEditText(parent : View, editText: EditText){
        hideKeyboard(editText.windowToken)
        editText.text.clear()
        editText.clearFocus()
        parent.visibility = View.GONE
    }

    private fun createMorePopup(popupManager: PopupManager, btnOpenMore: ImageButton){
        val popup = popupManager.createPopup(R.layout.popup_list_more, true, btnOpenMore, PopupAlignment.LEFT)

        val btnClearList : Button = popup.popupView.findViewById(R.id.btnClearList)
        btnClearList.setOnClickListener{
            listAdapter.deleteCheckedItems()
            popup.popupWindow.dismiss()
        }

        val btnRenameList : Button = popup.popupView.findViewById(R.id.btnRenameList)
        btnRenameList.setOnClickListener{
            renameHeader()
            popup.popupWindow.dismiss()
        }

        val btnDeleteList : Button = popup.popupView.findViewById(R.id.btnDeleteList)
        btnDeleteList.setOnClickListener {
            listAdapter.deleteList()
            popup.popupWindow.dismiss()
        }
    }

    private fun defineSafeWindow(){
        val contentLayout = findViewById<ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(contentLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = insets.left,
                top = insets.top,
                right = insets.right,
                bottom = insets.bottom
            )
            windowInsets
        }
    }

    private fun renameHeader(){
        val tvListTitle: TextView = findViewById(R.id.tvListTitle)
        val etListTitle: TextView = findViewById(R.id.etListTitle)
        val btnConfirmRename: ImageButton = findViewById(R.id.btnConfirmRename)
        val btnOpenMore: ImageButton = findViewById(R.id.btnMore)

        tvListTitle.visibility = View.GONE
        btnOpenMore.visibility = View.GONE
        etListTitle.visibility = View.VISIBLE
        btnConfirmRename.visibility = View.VISIBLE
        etListTitle.text = tvListTitle.text

        btnConfirmRename.setOnClickListener {
            etListTitle.visibility = View.GONE
            btnConfirmRename.visibility = View.GONE
            tvListTitle.visibility = View.VISIBLE
            btnOpenMore.visibility = View.VISIBLE

            val title: String = etListTitle.text.toString()

            saveTitle(title)
            tvListTitle.text = title

            hideKeyboard(etListTitle.windowToken)
        }
    }

    private fun saveTitle(title: String) {
        val sharedPref = getSharedPreferences("listPrefs", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("listTitle", title)
            apply()
        }
    }

    private fun loadTitle(): String {
        val title: String = getString(R.string.title)
        val sharedPref = getSharedPreferences("listPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("listTitle", title) ?: title
    }

    private fun hideKeyboard(windowToken: IBinder){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}