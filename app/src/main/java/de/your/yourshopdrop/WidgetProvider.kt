package de.your.yourshopdrop

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

//TODO: Widget, bei dem die letzte verwendete Liste angeziegt wird
class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.widget)

    // Beispiel: Text im Widget ändern
    views.setTextViewText(R.id.widget_text, "Widget aktualisiert!")

    // Optional: Intent zum Öffnen der App bei Klick auf das Widget
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE )
    views.setOnClickPendingIntent(R.id.widget_text, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
