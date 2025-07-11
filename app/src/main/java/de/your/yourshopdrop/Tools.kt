package de.your.yourshopdrop

import android.app.Activity
import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class Tools {
     companion object{

         fun blurView(view: View, radius: Float = 10f) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                 view.setRenderEffect(
                     RenderEffect.createBlurEffect(
                         radius, radius, Shader.TileMode.CLAMP
                     )
                 )
             }
         }

         fun removeBlur(view: View) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                 view.setRenderEffect(null)
             }
         }

         fun marginInPx(context: Activity, dp: Int): Int{
             val density = context.resources.displayMetrics.density
             val marginTopInPx = (dp * density).toInt()
             return marginTopInPx
         }

         fun hideKeyboard(textView: TextView){
             val imm = textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             imm.hideSoftInputFromWindow(textView.windowToken, 0)
         }

         fun showKeyboard(textView: TextView){
             val imm = textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT)
         }

         fun defineSafeWindow(context: Activity) {
             val contentLayout = context.findViewById<ConstraintLayout>(R.id.main)
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

         fun getKeyboardEnter(actionId: Int): Boolean {
             return actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
         }

         fun createSlideDownAnimation(duration: Long = 250): Animation {
             val animation = TranslateAnimation(
                 Animation.RELATIVE_TO_PARENT, 0f,
                 Animation.RELATIVE_TO_PARENT, 0f,
                 Animation.RELATIVE_TO_PARENT, -1f,
                 Animation.RELATIVE_TO_PARENT, 0f
             )
             animation.duration = duration
             return animation
         }

         fun createSlideUpAnimation(duration: Long = 250): Animation {
             val animation = TranslateAnimation(
                 Animation.RELATIVE_TO_PARENT, 0f,
                 Animation.RELATIVE_TO_PARENT, 0f,
                 Animation.RELATIVE_TO_PARENT, 0f,
                 Animation.RELATIVE_TO_PARENT, -1f
             )
             animation.duration = duration
             return animation
         }

     }
}