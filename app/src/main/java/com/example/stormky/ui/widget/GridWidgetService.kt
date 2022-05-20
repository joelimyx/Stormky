package com.example.stormky.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.stormky.R
import com.example.stormky.model.Current

class GridWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
        return StackRemoteViewFactory(p0, this.applicationContext)
    }
}
class StackRemoteViewFactory(
    intent: Intent,
    val context: Context,
    count: Int = 5,
    var hourly: List<Current> = listOf()
) : RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {
        hourly = listOf()
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.item_widget_grid)


        return remoteView
    }
    override fun onDataSetChanged() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return count
    }



    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}