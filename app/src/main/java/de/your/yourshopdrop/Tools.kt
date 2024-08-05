package de.your.yourshopdrop

import android.app.Activity
import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

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

     }
}