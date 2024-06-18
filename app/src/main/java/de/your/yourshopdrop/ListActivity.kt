package de.your.yourshopdrop

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListActivity : AppCompatActivity() {
    private lateinit var artikelAdapter: ArtikelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        setContentView(R.layout.list_activity)

        artikelAdapter = ArtikelAdapter(mutableListOf())
        val rvItemList : RecyclerView = findViewById(R.id.rvItemList)
        rvItemList.adapter = artikelAdapter
        rvItemList.layoutManager = LinearLayoutManager(this)


        val btnAddItem : ImageButton = findViewById(R.id.btnAddItem)

        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_add, null)

        // create the popup window
        val width = 700
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        btnAddItem.setOnClickListener{
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(this.window.decorView , Gravity.CENTER, 0, 0)

            val etItemHeader: EditText = popupView.findViewById(R.id.etItemHeader)
            val btnSaveItem: Button = popupView.findViewById(R.id.btnSaveItem)

            btnSaveItem.setOnClickListener {
                val artikelTitle = etItemHeader.text.toString()
                if (artikelTitle.isNotEmpty()) {
                    val artikel = Artikel(artikelTitle)
                    artikelAdapter.addArtikel(artikel)
                    etItemHeader.text.clear()
                    popupWindow.dismiss()
                }
            }

            // dismiss the popup window when touched
            popupView.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }
        }
    }


}