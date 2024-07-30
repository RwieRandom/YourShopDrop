package de.your.yourshopdrop

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeActions(
    private val adapter: ListAdapter,
    private val screen: Screen,
    private val listAdapter: ListAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val buttonWidth = 50f // Button width in dp
    var swipedViewHolder: RecyclerView.ViewHolder? = null

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (swipedViewHolder != null) {
            adapter.notifyItemChanged(swipedViewHolder!!.adapterPosition)
        }
        swipedViewHolder = viewHolder
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val itemView = viewHolder.itemView
        val buttonsWidth = dpToPx(buttonWidth * 2, itemView.context)
        return buttonsWidth / itemView.width
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val height = itemView.bottom - itemView.top

            val buttonWidthPx = dpToPx(buttonWidth, itemView.context)
            val maxDx = -buttonWidthPx * 2
            val constrainedDx = dX.coerceAtLeast(maxDx)

            val inflater = LayoutInflater.from(itemView.context)
            val buttonsView = inflater.inflate(R.layout.swipe_buttons, null, false)

            buttonsView.findViewById<ImageButton>(R.id.btnSwipeRename).setOnClickListener {
                handleButtonRenameClick(viewHolder.adapterPosition)
            }

            buttonsView.findViewById<ImageButton>(R.id.btnSwipeDelete).setOnClickListener {
                handleButtonDeleteClick(viewHolder.adapterPosition)
            }

            buttonsView.measure(View.MeasureSpec.makeMeasureSpec(itemView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
            buttonsView.layout(itemView.right + constrainedDx.toInt(), itemView.top,
                itemView.right + constrainedDx.toInt() + buttonsView.measuredWidth, itemView.bottom)

            // Buttons auf den Canvas zeichnen
            c.save()
            c.translate(itemView.right + constrainedDx, itemView.top.toFloat())
            buttonsView.draw(c)
            c.restore()

            super.onChildDraw(c, recyclerView, viewHolder, constrainedDx, dY, actionState, isCurrentlyActive)
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun handleButtonRenameClick(adapterPosition: Int) {
        btnDeleteItems(adapterPosition)
    }

    private fun handleButtonDeleteClick(adapterPosition: Int) {
        when (screen) {
            Screen.LISTS -> btnDeleteLists(adapterPosition)
            Screen.START -> btnDeleteItems(adapterPosition)
            else -> {}
        }
    }

    private fun dpToPx(dp: Float, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun btnDeleteLists(position: Int) {
        listAdapter.deleteItem(position)
    }

    private fun btnRenameLists(position: Int) {
        // Add logic for renaming lists
    }

    private fun btnDeleteItems(position: Int) {
        listAdapter.deleteItem(position)
    }

    private fun btnRenameItems(position: Int) {
        // Add logic for renaming items
    }
}
