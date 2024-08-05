package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout


//TODO: PopupManager wird zu ScreenInflator umbenannt, Dazu gibt es einen ScreenManager um alles zu regeln
class PopupManager(private val context: Activity) {

    private val inflater = LayoutInflater.from(context)

    data class Popup(val popupView: View, val popupWindow: PopupWindow)

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    fun createPopup(layout: Int): Popup {
        val popupView: View = inflater.inflate(layout, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)

        // Verhindert das Schließen des Popups bei Klick außerhalb
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = false
        popupWindow.isFocusable = true

        // Hintergrund-ImageView erstellen, um Touch-Ereignisse abzufangen
        val backgroundView = FrameLayout(context)

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.topMargin = Tools.marginInPx(context, 30)
        backgroundView.layoutParams = params
        backgroundView.setBackgroundColor(Color.parseColor("#80000000")) // Halbtransparenter Hintergrund
        backgroundView.isFocusableInTouchMode = false


        // Hintergrund-View zur Fensteransicht hinzufügen
        val parentView = context.window.decorView as ViewGroup
        parentView.addView(backgroundView)

        popupView.setOnTouchListener { _, _ -> true }
        backgroundView.setOnTouchListener { _, _ -> true }

        // Entferne die Hintergrund-View wenn das Popup geschlossen wird
        popupWindow.setOnDismissListener {
            parentView.removeView(backgroundView)
            onClosePopup()
        }

        popupWindow.showAtLocation(context.window.decorView, Gravity.TOP, 0, 0)

        return Popup(popupView, popupWindow)
    }

    private fun onClosePopup(){
        Tools.removeBlur(context.findViewById(R.id.scrollViewListItems))
        val selectedItemSettings: ConstraintLayout = context.findViewById(R.id.selectedNavbarItem_Settings)
        val selectedItemLists: ConstraintLayout = context.findViewById(R.id.selectedNavbarItem_Lists)
        selectedItemSettings.visibility = View.GONE
        selectedItemLists.visibility = View.GONE
    }
}
