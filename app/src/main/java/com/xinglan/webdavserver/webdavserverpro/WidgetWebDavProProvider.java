package com.xinglan.webdavserver.webdavserverpro;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import com.xinglan.webdavserver.R;
import com.xinglan.webdavserver.andromilton.BerryUtil;
import com.xinglan.webdavserver.webdavserverlib.WebdavService;
import com.xinglan.webdavserver.widgetutil.WidgetUtilProvider;

public class WidgetWebDavProProvider extends WidgetUtilProvider {
    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BerryUtil server = WebdavService.getServer();
        int image = server != null ? R.mipmap.on : R.mipmap.off;
        onUpdateImplementation(context, appWidgetManager, appWidgetIds, WidgetWebDavProProvider.class, image, WidgetWebDavProIntentReceiver.ChangeStatusAction);
    }
}
