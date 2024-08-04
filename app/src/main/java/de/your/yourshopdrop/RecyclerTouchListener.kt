package de.your.yourshopdrop

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerTouchListener(context: Context, private val recyclerView: RecyclerView, private val adapter: ListAdapter) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView = rv.findChildViewUnder(e.x, e.y)
        if (childView == null || !gestureDetector.onTouchEvent(e)) {
            adapter.clearSwipedPosition()
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        // No-op
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // No-op
    }
}
