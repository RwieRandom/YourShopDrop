import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.your.yourshopdrop.ListAdapter
import de.your.yourshopdrop.R

class SwipeActions(private val adapter: ListAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    //TODO: Button klick
    private val buttonWidth = 60f // Button-Breite in dp
    var swipedViewHolder: RecyclerView.ViewHolder? = null

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        // Nicht benötigt für Swipe-to-Reveal, Rückgabe von false
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Setze den Adapter zurück, um den Swipe rückgängig zu machen
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

            // Breite der Buttons auf 50dp setzen (in Pixel umrechnen)
            val buttonWidthPx = dpToPx(buttonWidth, itemView.context)
            val maxDx = -buttonWidthPx * 2

            // Begrenze das Swipe auf die maximale Breite der Buttons
            val constrainedDx = dX.coerceAtLeast(maxDx)

            // Inflater um das Layout zu laden
            val inflater = LayoutInflater.from(itemView.context)
            val buttonsView = inflater.inflate(R.layout.swipe_buttons, null, false)

            // Position und Größe der Buttons setzen
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

    // Hilfsfunktion zum Umrechnen von DP in Pixel
    private fun dpToPx(dp: Float, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }
}
