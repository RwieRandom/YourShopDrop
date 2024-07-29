package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow

//TODO: Fenster geht immer wieder zu, soll aber nicht zu gehen wenn man irgendwo anders hinklickt
class PopupManager(private val context: Activity) {

    private val inflater = LayoutInflater.from(context)

    data class Popup(val popupView: View, val popupWindow: PopupWindow)

    @SuppressLint("ClickableViewAccessibility")
    fun createPopup(layout: Int, canDismissOnTouch: Boolean): Popup {
        val popupView: View = inflater.inflate(layout, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)


//        if (canDismissOnTouch) {
//            popupView.setOnTouchListener { _, _ ->
//                popupWindow.dismiss()
//                true
//            }
//        }
        popupWindow.showAtLocation(context.window.decorView, Gravity.TOP, 0, 0)

        return Popup(popupView, popupWindow)
    }

    fun dismissPopup(popup: Popup) {
        popup.popupWindow.dismiss()
    }
}