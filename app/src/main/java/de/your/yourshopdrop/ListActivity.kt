package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
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

        enableEdgeToEdge()
        setContentView(R.layout.list_activity)

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

        val listItemManager = ListItemManager(this, "items.json")

        listAdapter = ListAdapter(listItemManager)
        val rvItemList : RecyclerView = findViewById(R.id.rvItemList)
        rvItemList.adapter = listAdapter
        rvItemList.layoutManager = LinearLayoutManager(this)


        val btnAddItem : Button = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener{
            val header : ConstraintLayout= findViewById(R.id.header)
            val popup = popupManager.createPopup(R.layout.popup_add_item, true)

            val etItemHeader: EditText = popup.popupView.findViewById(R.id.etItemHeader)
            val btnSaveItem: Button = popup.popupView.findViewById(R.id.btnSaveItem)

            btnSaveItem.setOnClickListener {
                val artikelTitle = etItemHeader.text.toString()
                if (artikelTitle.isNotEmpty()) {
                    val item = ListItem(artikelTitle)
                    listAdapter.add(item)
                    etItemHeader.text.clear()
                    popup.popupWindow.dismiss()
                }
            }
        }

        val btnOpenMore: ImageButton = findViewById(R.id.btnMore)
        btnOpenMore.setOnClickListener{
            val popup = popupManager.createPopup(R.layout.popup_list_more, true, btnOpenMore, PopupAlignment.LEFT)

            val btnClearList : Button = popup.popupView.findViewById(R.id.btnClearList)
            val btnRenameList : Button = popup.popupView.findViewById(R.id.btnRenameList)

            btnClearList.setOnClickListener{
                listAdapter.deleteCheckedItems()
            }

            btnRenameList.setOnClickListener{

            }



        }
    }


}