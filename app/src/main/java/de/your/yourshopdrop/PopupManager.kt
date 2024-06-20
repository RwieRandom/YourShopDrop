package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService


class PopupManager(private val inflater: LayoutInflater, private val context: Activity) {

    data class Popup(val popupView: View, val popupWindow: PopupWindow)

    @SuppressLint("ClickableViewAccessibility")
    fun createPopup(layout: Int, canDismissOnTouch: Boolean, anchorView: View?, alignment: PopupAlignment): Popup {
        val popupView: View = inflater.inflate(layout, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        if(canDismissOnTouch){
            popupView.setOnTouchListener { _, _ ->
                popupWindow.dismiss()
                true
            }
        }

        if(anchorView != null ){
            showPopup(popupView, popupWindow, anchorView, alignment)
        }

        return Popup(popupView, popupWindow)
    }

    private fun createPopup(layout: Int, canDismissOnTouch: Boolean, anchorView: View?):Popup{
        return createPopup(layout, canDismissOnTouch, anchorView, PopupAlignment.CENTER)
    }

    fun createPopup(layout: Int, canDismissOnTouch: Boolean):Popup{
        val popup:Popup = createPopup(layout, canDismissOnTouch, null)
        popup.popupWindow.showAtLocation(context.window.decorView , Gravity.CENTER, 0, 0)
        return popup
    }


    private fun showPopup(
        popupView: View,
        popupWindow: PopupWindow,
        anchorView: View,
        alignment: PopupAlignment
    ) {
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val x = when (alignment) {
            PopupAlignment.LEFT -> location[0]
            PopupAlignment.CENTER -> location[0] + (anchorView.width - popupView.measuredWidth) / 2
            PopupAlignment.RIGHT -> location[0] + anchorView.width - popupView.measuredWidth
        }
        val y = location[1] + anchorView.height

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y)
    }
}