package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow


class ScreenInflater(private val context: Activity) {

    private val inflater = LayoutInflater.from(context)

    data class Screen(val screenView: View, val popupWindow: PopupWindow)

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    fun createScreen(layout: Int): Screen {
        val screenView: View = inflater.inflate(layout, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(screenView, width, height, true)

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

        screenView.setOnTouchListener { _, _ -> true }
        backgroundView.setOnTouchListener { _, _ -> true }

        // Entferne die Hintergrund-View wenn das Popup geschlossen wird
        popupWindow.setOnDismissListener {
            parentView.removeView(backgroundView)
            ScreenManager(context, ItemAdapter(ItemManager(context)), ItemManager(context)).onScreenClose()
        }

        popupWindow.showAtLocation(context.window.decorView, Gravity.TOP, 0, 0)

        return Screen(screenView, popupWindow)
    }
}
