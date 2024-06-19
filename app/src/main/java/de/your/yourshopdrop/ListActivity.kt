package de.your.yourshopdrop

import android.annotation.SuppressLint
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

        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_add, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        btnAddItem.setOnClickListener{
            // show the popup window
            popupWindow.showAtLocation(this.window.decorView , Gravity.CENTER, 0, 0)

            val etItemHeader: EditText = popupView.findViewById(R.id.etItemHeader)
            val btnSaveItem: Button = popupView.findViewById(R.id.btnSaveItem)

            btnSaveItem.setOnClickListener {
                val artikelTitle = etItemHeader.text.toString()
                if (artikelTitle.isNotEmpty()) {
                    val item = ListItem(artikelTitle)
                    listAdapter.add(item)
                    etItemHeader.text.clear()
                    popupWindow.dismiss()
                }
            }

            // dismiss the popup window when touched
            popupView.setOnTouchListener { _, _ ->
                popupWindow.dismiss()
                true
            }
        }
    }


}