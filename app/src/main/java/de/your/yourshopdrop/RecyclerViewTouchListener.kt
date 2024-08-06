package de.your.yourshopdrop

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewTouchListener(
    context: Context,
    private val recyclerView: RecyclerView,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.OnItemTouchListener {

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
        val position = rv.getChildAdapterPosition(childView ?: return false)

        // Check if the touch event is within the rename layout
        if (adapter is ItemAdapter) {
            if (position == (adapter as ItemAdapter).getRenamePosition()) {
                val viewHolder = rv.findViewHolderForAdapterPosition(position) ?: return false
                val renameLayout = viewHolder.itemView.findViewById<View>(R.id.container_renameItem)
                if (isTouchInsideView(e, renameLayout)) {
                    return false
                }
            }
        } else if (adapter is ListAdapter) {
            // Assuming ListAdapter has similar methods for rename/swiped position
            if (position == (adapter as ListAdapter).getRenamePosition()) {
                val viewHolder = rv.findViewHolderForAdapterPosition(position) ?: return false
                val renameLayout = viewHolder.itemView.findViewById<View>(R.id.container_renameItem)
                if (isTouchInsideView(e, renameLayout)) {
                    return false
                }
            }
        }

        if (childView == null || !gestureDetector.onTouchEvent(e)) {
            if (adapter is ItemAdapter) {
                (adapter as ItemAdapter).clearSwipedPosition()
                (adapter as ItemAdapter).clearRenamePosition()
            } else if (adapter is ListAdapter) {
                (adapter as ListAdapter).clearSwipedPosition()
                (adapter as ListAdapter).clearRenamePosition()
            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        // No-op
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // No-op
    }

    private fun isTouchInsideView(e: MotionEvent, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = e.rawX + recyclerView.scrollX
        val y = e.rawY + recyclerView.scrollY
        return x >= location[0] && x <= location[0] + view.width &&
                y >= location[1] && y <= location[1] + view.height
    }
}
