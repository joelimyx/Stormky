package com.example.stormky.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.example.stormky.MainActivity
import com.example.stormky.R

/**
 * Implementation of App Widget functionality.
 */
class StormkyWidget : AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        const val widgetKey = "fromWidget"
    }
}

@RequiresApi(Build.VERSION_CODES.S)
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val hourlyIntent = Intent(context, MainActivity::class.java).apply {
        putExtra(StormkyWidget.widgetKey, true)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        1,
        hourlyIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_layout).apply {
//        setEmptyView(R.id.grid_widget, R.id.text_widget)
        setOnClickPendingIntent(R.id.widget_container, pendingIntent)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}