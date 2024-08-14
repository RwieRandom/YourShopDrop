package de.your.yourshopdrop

import android.annotation.SuppressLint
import android.app.Activity
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow


class ScreenInflater(private val context: Activity, private val screenManager: ScreenManager) {

    private val inflater = LayoutInflater.from(context)

    data class Screen(val screenView: View, val popupWindow: PopupWindow)

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    fun createScreen(layout: Int): Screen {
        val screenView: View = inflater.inflate(layout, null)

        val slideDownAnimation = Tools.createSlideDownAnimation()
        screenView.startAnimation(slideDownAnimation)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(screenView, width, height, true)

        val backgroundView = FrameLayout(context)

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.topMargin = Tools.marginInPx(context, 30)
        backgroundView.layoutParams = params
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.colorBackgroundFloating, typedValue, true)
        val colorBackgroundFloating = typedValue.data
        backgroundView.setBackgroundColor(colorBackgroundFloating)

        backgroundView.isFocusableInTouchMode = false

        val parentView = context.window.decorView as ViewGroup
        parentView.addView(backgroundView)

        screenView.setOnTouchListener { _, _ -> true }
        backgroundView.setOnTouchListener { _, _ -> true }

        popupWindow.setOnDismissListener {
            parentView.removeView(backgroundView)
            screenManager.onScreenClose()
        }

        popupWindow.showAtLocation(context.window.decorView, Gravity.TOP, 0, 0)

        return Screen(screenView, popupWindow)
    }
}
