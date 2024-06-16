package de.your.yourshopdrop

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var artikelAdapter: ArtikelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        setContentView(R.layout.list_activity)

        artikelAdapter = ArtikelAdapter(mutableListOf())

        val btnAddArtikel : ImageButton = findViewById(R.id.btnAddItem)

        btnAddArtikel.setOnClickListener{

            // inflate the layout of the popup window
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.popup_add, null)


            // create the popup window
            val width = 700
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)


            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(this.window.decorView , Gravity.CENTER, 0, 0)


            // dismiss the popup window when touched
            popupView.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }
        }


        /*val rvEinkaufsliste: RecyclerView = findViewById(R.id.rvEinkaufsliste)
        val btnAddArtikel : Button = findViewById(R.id.btnAddArtikel)
        val btnDeleteArtikel : Button = findViewById(R.id.btnDeleteArtikel)
        val etArtikel : EditText = findViewById(R.id.etArtikelname)

        rvEinkaufsliste.adapter = artikelAdapter
        rvEinkaufsliste.layoutManager = LinearLayoutManager(this)

        btnAddArtikel.setOnClickListener{
            val artikelTitle = etArtikel.text.toString()
            if(artikelTitle.isNotEmpty()){
                val artikel = Artikel(artikelTitle)
                artikelAdapter.addArtikel(artikel)
                etArtikel.text.clear()
            }
        }

        btnDeleteArtikel.setOnClickListener{
            artikelAdapter.deleteDoneArtikel()
        }*/
    }
}